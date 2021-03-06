// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.soap.account.type;

import com.google.common.base.MoreObjects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import com.zimbra.common.gql.GqlConstants;
import com.zimbra.common.soap.AccountConstants;

import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;

/*
 * Used in GetInfoResponse if ZimbraLicenseExtension is installed
 */
@XmlAccessorType(XmlAccessType.NONE)
@GraphQLType(name=GqlConstants.CLASS_LICENSE_ATTR, description="License attributes")
public class LicenseAttr {

    /**
     * @zm-api-field-tag license-attr-name
     * @zm-api-field-description Current valid values are "SMIME" and "VOICE"
     */
    @XmlAttribute(name=AccountConstants.A_NAME /* name */, required=true)
    private String name;

    /**
     * @zm-api-field-tag license-attr-value-TRUE|FALSE
     * @zm-api-field-description Value - value is "TRUE" or "FALSE"
     */
    @XmlValue
    private String content;

    public LicenseAttr() {
    }

    public void setName(String name) { this.name = name; }
    public void setContent(String content) { this.content = content; }
    @GraphQLNonNull
    @GraphQLQuery(name=GqlConstants.NAME, description="Name of license attribute")
    public String getName() { return name; }
    @GraphQLQuery(name=GqlConstants.CONTENT, description="Value of license attribute")
    public String getContent() { return content; }

    public MoreObjects.ToStringHelper addToStringInfo(MoreObjects.ToStringHelper helper) {
        return helper
            .add("name", name)
            .add("content", content);
    }

    @Override
    public String toString() {
        return addToStringInfo(MoreObjects.toStringHelper(this)).toString();
    }
}
