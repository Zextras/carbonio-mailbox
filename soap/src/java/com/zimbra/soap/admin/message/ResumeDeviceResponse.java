// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.soap.admin.message;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.zimbra.common.soap.SyncAdminConstants;
import com.zimbra.common.soap.SyncConstants;
import com.zimbra.soap.admin.type.DeviceStatusInfo;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name=SyncAdminConstants.E_RESUME_DEVICE_RESPONSE)
@XmlType(propOrder = {})
public class ResumeDeviceResponse {

    /**
     * @zm-api-field-description Information about device status
     */
    @XmlElement(name=SyncConstants.E_DEVICE /* device */, required=false)
    private List<DeviceStatusInfo> devices = Lists.newArrayList();

    public ResumeDeviceResponse() {
    }

    public void setDevices(Iterable<DeviceStatusInfo> devices) {
        this.devices.clear();
        if (devices != null) {
            Iterables.addAll(this.devices, devices);
        }
    }

    public void addDevice(DeviceStatusInfo device) {
        this.devices.add(device);
    }

    public List<DeviceStatusInfo> getDevices() {
        return Collections.unmodifiableList(devices);
    }

    public MoreObjects.ToStringHelper addToStringInfo(MoreObjects.ToStringHelper helper) {
        return helper.add("devices", devices);
    }

    @Override
    public String toString() {
        return addToStringInfo(MoreObjects.toStringHelper(this)).toString();
    }
}
