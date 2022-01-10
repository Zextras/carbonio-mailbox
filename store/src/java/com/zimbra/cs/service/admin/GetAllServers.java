// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

/*
 * Created on Jun 17, 2004
 */
package com.zimbra.cs.service.admin;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.soap.AdminConstants;
import com.zimbra.common.soap.Element;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.cs.account.Server;
import com.zimbra.cs.account.accesscontrol.AdminRight;
import com.zimbra.cs.account.accesscontrol.Rights.Admin;
import com.zimbra.soap.ZimbraSoapContext;

/**
 * @author schemers
 */
public class GetAllServers extends AdminDocumentHandler {

    public static final String BY_NAME = "name";
    public static final String BY_ID = "id";

	@Override
    public Element handle(Element request, Map<String, Object> context) throws ServiceException {

        ZimbraSoapContext zsc = getZimbraSoapContext(context);
        Provisioning prov = Provisioning.getInstance();

        String service = request.getAttribute(AdminConstants.A_SERVICE, null);
        String alwaysOnClusterId = request.getAttribute(AdminConstants.A_ALWAYSONCLUSTER_ID, null);

        boolean applyConfig = request.getAttributeBool(AdminConstants.A_APPLY_CONFIG, true);
        List<Server> servers;
        if (alwaysOnClusterId == null) {
            servers = prov.getAllServers(service);
        } else {
            servers = prov.getAllServers(service, alwaysOnClusterId);
        }

        AdminAccessControl aac = AdminAccessControl.getAdminAccessControl(zsc);

        Element response = zsc.createElement(AdminConstants.GET_ALL_SERVERS_RESPONSE);
        for (Iterator<Server> it = servers.iterator(); it.hasNext(); ) {
            Server server = it.next();
            if (aac.hasRightsToList(server, Admin.R_listServer, null))
                GetServer.encodeServer(response, server, applyConfig, null, aac.getAttrRightChecker(server));
        }

	    return response;
	}

    @Override
    public void docRights(List<AdminRight> relatedRights, List<String> notes) {
        relatedRights.add(Admin.R_listServer);
        relatedRights.add(Admin.R_getServer);

        notes.add(AdminRightCheckPoint.Notes.LIST_ENTRY);
    }
}
