// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.soap.mail.message;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.zimbra.common.soap.MailConstants;
import com.zimbra.soap.mail.type.DismissAlarm;
import com.zimbra.soap.mail.type.DismissAppointmentAlarm;
import com.zimbra.soap.mail.type.DismissTaskAlarm;

/**
 * @zm-api-command-auth-required true
 * @zm-api-command-admin-auth-required false
 * @zm-api-command-description Dismiss calendar item alarm
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name=MailConstants.E_DISMISS_CALITEM_ALARM_REQUEST)
public class DismissCalendarItemAlarmRequest {

    /**
     * @zm-api-field-description Details of alarms to dismiss
     */
    @XmlElements({
        @XmlElement(name=MailConstants.E_APPOINTMENT /* appt */, type=DismissAppointmentAlarm.class),
        @XmlElement(name=MailConstants.E_TASK /* task */, type=DismissTaskAlarm.class)
    })
    private List<DismissAlarm> alarms = Lists.newArrayList();

    public DismissCalendarItemAlarmRequest() {
    }

    public void setAlarms(Iterable <DismissAlarm> alarms) {
        this.alarms.clear();
        if (alarms != null) {
            Iterables.addAll(this.alarms,alarms);
        }
    }

    public DismissCalendarItemAlarmRequest addAlarm(DismissAlarm alarm) {
        this.alarms.add(alarm);
        return this;
    }

    public List<DismissAlarm> getAlarms() {
        return Collections.unmodifiableList(alarms);
    }

    public MoreObjects.ToStringHelper addToStringInfo(MoreObjects.ToStringHelper helper) {
        return helper
            .add("alarms", alarms);
    }

    @Override
    public String toString() {
        return addToStringInfo(MoreObjects.toStringHelper(this)).toString();
    }
}
