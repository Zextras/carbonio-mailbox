// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.cs.service.account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.soap.AccountConstants;
import com.zimbra.common.soap.Element;
import com.zimbra.common.util.StringUtil;
import com.zimbra.cs.account.Account;
import com.zimbra.cs.account.AuthToken;
import com.zimbra.cs.account.AuthTokenException;
import com.zimbra.cs.session.Session;
import com.zimbra.cs.session.SessionCache;
import com.zimbra.cs.session.SoapSession;
import com.zimbra.soap.JaxbUtil;
import com.zimbra.soap.SoapServlet;
import com.zimbra.soap.ZimbraSoapContext;
import com.zimbra.soap.account.message.EndSessionRequest;

/**
 * End the current session immediately cleaning up all resources used by the session
 * including the notification buffer and logging the session out from IM if applicable
 */
public class EndSession extends AccountDocumentHandler {

    @Override
    public Element handle(Element request, Map<String, Object> context)
    throws ServiceException {
        ZimbraSoapContext zsc = getZimbraSoapContext(context);
        EndSessionRequest req = JaxbUtil.elementToJaxb(request);
        String sessionId = req.getSessionId();
        boolean clearCookies = req.isLogOff();
        boolean clearAllSessions = req.isClearAllSoapSessions();
        boolean excludeCurrrentSession = req.isExcludeCurrentSession();
        Account account = getAuthenticatedAccount(zsc);

        if (clearAllSessions) {
            String currentSessionId = null;
            if (excludeCurrrentSession && zsc.hasSession()) {
                Session currentSession = getSession(zsc);
                currentSessionId = currentSession.getSessionId();
            }
            Collection<Session> sessionCollection = SessionCache.getSoapSessions(account.getId());
            if (sessionCollection != null) {
                List<Session> sessions = new ArrayList<Session>(sessionCollection);
                Iterator<Session> itr = sessions.iterator();
                while (itr.hasNext()) {
                    Session session = itr.next();
                    itr.remove();
                    clearSession(session, currentSessionId);
                }
            }
        } else if (!StringUtil.isNullOrEmpty(sessionId)) {
            Session s = SessionCache.lookup(sessionId, account.getId());
            if (s == null) {
                throw ServiceException.FAILURE("Failed to find session with given sessionId", null);
            } else {
                clearSession(s, null);
            }
        } else {
            if (zsc.hasSession()) {
                Session s = getSession(zsc);
                endSession(s);
            }
            if (clearCookies || account.isForceClearCookies()) {
                context.put(SoapServlet.INVALIDATE_COOKIES, true);
                try {
                    AuthToken at = zsc.getAuthToken();
                    HttpServletRequest httpReq = (HttpServletRequest) context.get(SoapServlet.SERVLET_REQUEST);
                    HttpServletResponse httpResp = (HttpServletResponse) context.get(SoapServlet.SERVLET_RESPONSE);
                    at.encode(httpReq, httpResp, true);
                    at.deRegister();
                } catch (AuthTokenException e) {
                    throw ServiceException.FAILURE("Failed to de-register an auth token", e);
                }
            }
        }

        Element response = zsc.createElement(AccountConstants.END_SESSION_RESPONSE);
        return response;
    }

    private void clearSession(Session session, String currentSessionId) throws ServiceException {
        if(session instanceof SoapSession && !session.getSessionId().equalsIgnoreCase(currentSessionId)) {
            AuthToken at = ((SoapSession) session).getAuthToken();
            if (at != null) {
                try {
                    at.deRegister();
                } catch (AuthTokenException e) {
                    throw ServiceException.FAILURE("Failed to de-register an auth token", e);
                }
            }
            endSession(session);
        }
    }

}
