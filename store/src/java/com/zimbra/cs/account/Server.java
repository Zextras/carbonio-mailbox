// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

/*
 * Created on Sep 23, 2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.zimbra.cs.account;

import java.util.Map;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.util.ZimbraLog;

/**
 * @author schemers
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Server extends ZAttrServer {

    public Server(String name, String id, Map<String,Object> attrs, Map<String,Object> defaults, Provisioning prov) {
        super(name, id, attrs, defaults, prov);
        try {
            AlwaysOnCluster aoc = prov.getAlwaysOnCluster(this);
            if (aoc != null) {
                setOverrideDefaults(aoc.getServerOverrides());
            }
        } catch (ServiceException se) {
            ZimbraLog.account.warn("failed to get initialize server entry", se);
        }
    }

    @Override
    public EntryType getEntryType() {
        return EntryType.SERVER;
    }

    public void deleteServer(String zimbraId) throws ServiceException {
        getProvisioning().deleteServer(getId());
    }

    public void modify(Map<String, Object> attrs) throws ServiceException {
        getProvisioning().modifyAttrs(this, attrs);
    }

    /*
     * compare only proto and host, ignore port, because if port on the server was changed we
     * still want the change to go through.
     */
    public boolean mailTransportMatches(String mailTransport) {
        // if there is no mailTransport, it sure "matches"
        if (mailTransport == null)
            return true;

        String serviceName = getAttr(Provisioning.A_zimbraServiceHostname, null);

        String[] parts = mailTransport.split(":");
        if (serviceName != null && parts.length == 3) {
            if (parts[0].equalsIgnoreCase("lmtp") && parts[1].equals(serviceName))
                return true;
        }

        return false;
    }

    public boolean hasMailboxService() {
        return getMultiAttrSet(Provisioning.A_zimbraServiceEnabled).contains(Provisioning.SERVICE_MAILBOX);
    }

    public boolean hasProxyService() {
        return getMultiAttrSet(Provisioning.A_zimbraServiceEnabled).contains(Provisioning.SERVICE_PROXY);
    }

    public boolean hasWebClientService() {
        // Figure out if this a pre 8.5 server (i.e if zimbraServerVersion is not set)
        String version = this.getAttr(Provisioning.A_zimbraServerVersion, null);
        if (version != null) {
            return getMultiAttrSet(Provisioning.A_zimbraServiceEnabled).contains(Provisioning.SERVICE_WEBCLIENT);
        } else {
            return getMultiAttrSet(Provisioning.A_zimbraServiceEnabled).contains(Provisioning.SERVICE_MAILBOX);
        }
    }

    public boolean hasAdminClientService() {
        // Figure out if this a pre 8.5 server (i.e if zimbraServerVersion is not set)
        String version = this.getAttr(Provisioning.A_zimbraServerVersion, null);
        if (version != null) {
            return getMultiAttrSet(Provisioning.A_zimbraServiceEnabled).contains(Provisioning.SERVICE_ADMINCLIENT);
        } else {
            return getMultiAttrSet(Provisioning.A_zimbraServiceEnabled).contains(Provisioning.SERVICE_MAILBOX);
        }
    }

    public boolean hasMailClientService() {
        // Figure out if this a pre 8.5 server (i.e if zimbraServerVersion is not set)
        String version = this.getAttr(Provisioning.A_zimbraServerVersion, null);
        if (version != null) {
            return getMultiAttrSet(Provisioning.A_zimbraServiceEnabled).contains(Provisioning.SERVICE_MAILCLIENT);
        } else {
            return getMultiAttrSet(Provisioning.A_zimbraServiceEnabled).contains(Provisioning.SERVICE_MAILBOX);
        }
    }

    public boolean hasZimletService() {
        // Figure out if this a pre 8.5 server (i.e if zimbraServerVersion is not set)
        String version = this.getAttr(Provisioning.A_zimbraServerVersion, null);
        if (version != null) {
            return getMultiAttrSet(Provisioning.A_zimbraServiceEnabled).contains(Provisioning.SERVICE_ZIMLET);
        } else {
            return getMultiAttrSet(Provisioning.A_zimbraServiceEnabled).contains(Provisioning.SERVICE_MAILBOX);
        }
    }

    public boolean isLocalServer() throws ServiceException {
        Server localServer = getProvisioning().getLocalServer();
        return getId() != null && getId().equals(localServer.getId());
    }

}
