// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.soap.admin.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.MoreObjects;
import com.zimbra.common.soap.AdminConstants;
import com.zimbra.common.soap.HeaderConstants;
import com.zimbra.soap.json.jackson.annotate.ZimbraJsonAttribute;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name=AdminConstants.E_AUTH_RESPONSE)
public class AuthResponse {

    /**
     * @zm-api-field-tag auth-token
     * @zm-api-field-description Auth Token
     */
    @XmlElement(name=AdminConstants.E_AUTH_TOKEN /* authToken */, required=true)
    private String authToken;

    /**
     * @zm-api-field-description if client is CSRF token enabled , the CSRF token Returned only when client says it is CSRF enabled .
     */
    @XmlElement(name = HeaderConstants.E_CSRFTOKEN /* CSRF token */, required = false)
    private String csrfToken;

    /**
     * @zm-api-field-tag auth-lifetime
     * @zm-api-field-description Life time for the authorization
     */
    @ZimbraJsonAttribute
    @XmlElement(name=AdminConstants.E_LIFETIME /* lifetime */, required=true)
    private long lifetime;

    public AuthResponse() {
    }

    public void setAuthToken(String authToken) { this.authToken = authToken; }
    public void setLifetime(long lifetime) { this.lifetime = lifetime; }
    public String getAuthToken() { return authToken; }
    public long getLifetime() { return lifetime; }

    public MoreObjects.ToStringHelper addToStringInfo(MoreObjects.ToStringHelper helper) {
        return helper
            .add("authToken", authToken)
            .add("lifetime", lifetime);
    }

    @Override
    public String toString() {
        return addToStringInfo(MoreObjects.toStringHelper(this)).toString();
    }

    /**
     * @return the csrfToken
     */
    public String getCsrfToken() {
        return csrfToken;
    }

    /**
     * @param csrfToken
     *            the csrfToken to set
     */
    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

}
