// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.soap.mail.type;

import com.google.common.base.MoreObjects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.zimbra.common.soap.MailConstants;
import com.zimbra.soap.type.ZmBoolean;

@XmlAccessorType(XmlAccessType.NONE)
public class PurgeRevisionSpec {

    /**
     * @zm-api-field-tag item-id
     * @zm-api-field-description Item ID
     */
    @XmlAttribute(name=MailConstants.A_ID /* id */, required=true)
    private final String id;

    /**
     * @zm-api-field-tag revision
     * @zm-api-field-description Revision
     */
    @XmlAttribute(name=MailConstants.A_VERSION /* ver */, required=true)
    private final int version;

    /**
     * @zm-api-field-tag include-older-revs
     * @zm-api-field-description When set, the server will purge all the old revisions inclusive of the revision
     * specified in the request.
     */
    @XmlAttribute(name=MailConstants.A_INCLUDE_OLDER_REVISIONS /* includeOlderRevisions */, required=false)
    private ZmBoolean includeOlderRevisions;

    /**
     * no-argument constructor wanted by JAXB
     */
    @SuppressWarnings("unused")
    private PurgeRevisionSpec() {
        this((String) null, -1);
    }

    public PurgeRevisionSpec(String id, int version) {
        this.id = id;
        this.version = version;
    }

    public void setIncludeOlderRevisions(Boolean includeOlderRevisions) {
        this.includeOlderRevisions = ZmBoolean.fromBool(includeOlderRevisions);
    }
    public String getId() { return id; }
    public int getVersion() { return version; }
    public Boolean getIncludeOlderRevisions() { return ZmBoolean.toBool(includeOlderRevisions); }

    public MoreObjects.ToStringHelper addToStringInfo(MoreObjects.ToStringHelper helper) {
        return helper
            .add("id", id)
            .add("version", version)
            .add("includeOlderRevisions", includeOlderRevisions);
    }

    @Override
    public String toString() {
        return addToStringInfo(MoreObjects.toStringHelper(this)).toString();
    }
}
