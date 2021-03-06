// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.soap.admin.message;

import com.google.common.base.MoreObjects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.zimbra.common.soap.AdminConstants;

/**
 * @zm-api-command-auth-required true
 * @zm-api-command-admin-auth-required true
 * @zm-api-command-description Get attribute information
 * @zm-api-request-description Only one of <b>attrs</b> or <b>entryTypes</b> can be specified.
 * <br />
 * If both are specified, INVALID_REQUEST will be thrown.
 * <br />
 * If neither is specified, all attributes will be returned.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name=AdminConstants.E_GET_ATTRIBUTE_INFO_REQUEST)
public class GetAttributeInfoRequest {

    /**
     * @zm-api-field-tag attrs-to-return
     * @zm-api-field-description Comma separated list of attributes to return
     */
    @XmlAttribute(name=AdminConstants.A_ATTRS /* attrs */, required=false)
    private String attrs;

    // Comma Separated list
    /**
     * @zm-api-field-tag entry-types
     * @zm-api-field-description Comma separated list of entry types.  Attributes on the specified entry types will
     * be returned.
     * <br />
     * valid entry types:
     * <pre>
     *     account,alias,distributionList,cos,globalConfig,domain,server,mimeEntry,zimletEntry,
     *     calendarResource,identity,dataSource,pop3DataSource,imapDataSource,rssDataSource,
     *     liveDataSource,galDataSource,signature,xmppComponent,aclTarget.oauth2DataSource
     * </pre>
     */
    @XmlAttribute(name=AdminConstants.A_ENTRY_TYPES /* entryTypes */, required=false)
    private String entryTypes;

    public GetAttributeInfoRequest() {
    }

    public void setAttrs(String attrs) { this.attrs = attrs; }
    public void setEntryTypes(String entryTypes) {
        this.entryTypes = entryTypes;
    }
    public String getAttrs() { return attrs; }
    public String getEntryTypes() { return entryTypes; }

    public MoreObjects.ToStringHelper addToStringInfo(
                MoreObjects.ToStringHelper helper) {
        return helper
            .add("attrs", attrs)
            .add("entryTypes", entryTypes);
    }

    @Override
    public String toString() {
        return addToStringInfo(MoreObjects.toStringHelper(this))
                .toString();
    }
}
