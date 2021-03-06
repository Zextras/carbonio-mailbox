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

@XmlAccessorType(XmlAccessType.NONE)
public class DeviceInfo {

    /**
     * @zm-api-field-tag device-id
     * @zm-api-field-description Device ID
     */
    @XmlAttribute(name=MailConstants.A_ID /* id */, required=false)
    private String id;

    /**
     * @zm-api-field-tag device-name
     * @zm-api-field-description Device name
     */
    @XmlAttribute(name=MailConstants.A_NAME /* name */, required=false)
    private String name;

    /**
     * @zm-api-field-tag status-enabled|disabled|locked|wiped
     * @zm-api-field-description Status - <b>enabled|disabled|locked|wiped</b>
     */
    @XmlAttribute(name=MailConstants.A_STATUS /* status */, required=false)
    private String status;

    /**
     * @zm-api-field-tag created-timestamp
     * @zm-api-field-description Created timestamp
     */
    @XmlAttribute(name=MailConstants.A_CREATED /* created */, required=false)
    private Long created;

    /**
     * @zm-api-field-tag accessed-timestamp
     * @zm-api-field-description Accessed timestamp
     */
    @XmlAttribute(name=MailConstants.A_ACCESSED /* accessed */, required=false)
    private Long accessed;

    public DeviceInfo() {
    }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setStatus(String status) { this.status = status; }
    public void setCreated(Long created) { this.created = created; }
    public void setAccessed(Long accessed) { this.accessed = accessed; }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getStatus() { return status; }
    public Long getCreated() { return created; }
    public Long getAccessed() { return accessed; }

    public MoreObjects.ToStringHelper addToStringInfo(MoreObjects.ToStringHelper helper) {
        return helper
            .add("id", id)
            .add("name", name)
            .add("status", status)
            .add("created", created)
            .add("accessed", accessed);
    }

    @Override
    public String toString() {
        return addToStringInfo(MoreObjects.toStringHelper(this)).toString();
    }
}
