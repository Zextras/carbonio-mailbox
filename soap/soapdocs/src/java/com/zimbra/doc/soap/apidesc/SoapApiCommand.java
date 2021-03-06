// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.doc.soap.apidesc;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.zimbra.doc.soap.Command;

public class SoapApiCommand
implements Comparable<SoapApiCommand> {

    private String name;
    private String namespace;
    private String description;
    private String deprecationInformation;
    private String authRequiredDescription;
    private String adminAuthRequiredDescription;
    private boolean networkEditionOnly;
    private SoapApiElement request;
    private SoapApiElement response;

    /* no-argument constructor needed for deserialization */
    @SuppressWarnings("unused")
    private SoapApiCommand() {
    }

    public SoapApiCommand(Command cmd) {
        name = cmd.getName();
        namespace = cmd.getNamespace();
        description = cmd.getDescription();
        request = new SoapApiElement(cmd.getRequest());
        response = new SoapApiElement(cmd.getResponse());
        cmd.getResponse();
        // cmd.getShortDescription();
        deprecationInformation = cmd.getDeprecation();
        if (Strings.isNullOrEmpty(deprecationInformation)) {
            deprecationInformation = null;
        }
        networkEditionOnly = cmd.isNetworkEdition();
        authRequiredDescription = cmd.getAuthRequiredDescription();
        adminAuthRequiredDescription = cmd.getAdminAuthRequiredDescription();
    }

    public String getName() { return name; }
    public String getNamespace() { return namespace; }
    public String getDescription() { return description; }
    public String getDeprecationInformation() { return deprecationInformation; }
    public boolean isNetworkEditionOnly() { return networkEditionOnly; }
    public String getAuthRequiredDescription() { return authRequiredDescription; }
    public String getAdminAuthRequiredDescription() { return adminAuthRequiredDescription; }

    @JsonIgnore
    public String getId() {
        StringBuilder id = new StringBuilder();
        if (namespace != null) {
            id.append(namespace).append(":");
        }
        id.append(name);
        return id.toString();
    }

    @JsonIgnore
    public String getDocApiLinkFragment() {
        StringBuilder sb = new StringBuilder();
        sb.append(namespace.replaceFirst("urn:", "")).append('/').append(name).append(".html");
        return sb.toString();
    }

    public SoapApiElement getRequest() { return request; }
    public SoapApiElement getResponse() { return response; }

    @Override
    public int compareTo(SoapApiCommand o) {
        return request.getJaxb().compareTo(o.request.getJaxb());
    }
}
