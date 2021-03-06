// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.soap.voice.type;

import com.google.common.base.MoreObjects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.zimbra.common.soap.AccountConstants;
import com.zimbra.common.soap.MailConstants;

@XmlAccessorType(XmlAccessType.NONE)
public class VoiceFolder {

    /**
     * @zm-api-field-tag folder-name
     * @zm-api-field-description Folder name
     */
    @XmlAttribute(name=AccountConstants.A_NAME /* name */, required=true)
    private String name;

    /**
     * @zm-api-field-tag phone-ID
     * @zm-api-field-description Phone ID
     */
    @XmlAttribute(name=MailConstants.A_ID /* id */, required=true)
    private String phoneId;

    /**
     * @zm-api-field-tag folder-ID
     * @zm-api-field-description Folder ID
     */
    @XmlAttribute(name=MailConstants.A_FOLDER /* l */, required=true)
    private String folderId;

    /**
     * @zm-api-field-tag view
     * @zm-api-field-description View
     */
    @XmlAttribute(name=MailConstants.A_DEFAULT_VIEW /* view */, required=false)
    private String view;

    /**
     * @zm-api-field-tag num-unread-voice-msgs
     * @zm-api-field-description Number of unread voice messages
     * <br />
     * Only present for Trash and Voicemail Inbox
     */
    @XmlAttribute(name=MailConstants.A_UNREAD /* u */, required=false)
    private Long numUnreadMsgs;

    /**
     * @zm-api-field-tag total-num-voice-msgs
     * @zm-api-field-description Total number of voice messages
     * <br />
     * Only present for Trash and Voicemail Inbox
     */
    @XmlAttribute(name=MailConstants.A_NUM /* n */, required=false)
    private Long numTotalMsgs;

    public VoiceFolder() {
    }

    public void setName(String name) { this.name = name; }
    public void setPhoneId(String phoneId) { this.phoneId = phoneId; }
    public void setFolderId(String folderId) { this.folderId = folderId; }
    public void setView(String view) { this.view = view; }
    public void setNumUnreadMsgs(Long numUnreadMsgs) { this.numUnreadMsgs = numUnreadMsgs; }
    public void setNumTotalMsgs(Long numTotalMsgs) { this.numTotalMsgs = numTotalMsgs; }
    public String getName() { return name; }
    public String getPhoneId() { return phoneId; }
    public String getFolderId() { return folderId; }
    public String getView() { return view; }
    public Long getNumUnreadMsgs() { return numUnreadMsgs; }
    public Long getNumTotalMsgs() { return numTotalMsgs; }

    public MoreObjects.ToStringHelper addToStringInfo(MoreObjects.ToStringHelper helper) {
        return helper
            .add("name", name)
            .add("phoneId", phoneId)
            .add("folderId", folderId)
            .add("view", view)
            .add("numUnreadMsgs", numUnreadMsgs)
            .add("numTotalMsgs", numTotalMsgs);
    }

    @Override
    public String toString() {
        return addToStringInfo(MoreObjects.toStringHelper(this)).toString();
    }
}
