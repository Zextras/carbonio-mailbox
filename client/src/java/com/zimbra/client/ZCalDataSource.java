// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.client;

import org.json.JSONException;

import com.zimbra.common.soap.Element;
import com.zimbra.common.soap.MailConstants;
import com.zimbra.soap.admin.type.DataSourceType;
import com.zimbra.soap.mail.type.CalDataSourceNameOrId;
import com.zimbra.soap.mail.type.DataSourceNameOrId;
import com.zimbra.soap.mail.type.MailCalDataSource;
import com.zimbra.soap.type.CalDataSource;
import com.zimbra.soap.type.DataSource;
import com.zimbra.soap.type.DataSources;

public class ZCalDataSource extends ZDataSource implements ToZJSONObject {

    public ZCalDataSource(String name, String folderId, boolean enabled) {
        data = DataSources.newCalDataSource();
        data.setName(name);
        data.setFolderId(folderId);
        data.setEnabled(enabled);
    }
    
    public ZCalDataSource(CalDataSource data) {
        this.data = DataSources.newCalDataSource(data);
    }
    
    @Override
    public DataSourceType getType() {
        return DataSourceType.cal;
    }
    
    public String getFolderId() {
        return data.getFolderId();
    }

    @Deprecated
    public Element toElement(Element parent) {
        Element src = parent.addElement(MailConstants.E_DS_CAL);
        src.addAttribute(MailConstants.A_ID, data.getId());
        src.addAttribute(MailConstants.A_NAME, data.getName());
        src.addAttribute(MailConstants.A_DS_IS_ENABLED, data.isEnabled());
        src.addAttribute(MailConstants.A_FOLDER, data.getFolderId());
        return src;
    }

    @Deprecated
    public Element toIdElement(Element parent) {
        Element src = parent.addElement(MailConstants.E_DS_CAL);
        src.addAttribute(MailConstants.A_ID, getId());
        return src;
    }

    @Override
    public DataSource toJaxb() {
        MailCalDataSource jaxbObject = new MailCalDataSource();
        jaxbObject.setId(data.getId());
        jaxbObject.setName(data.getName());
        jaxbObject.setFolderId(data.getFolderId());
        jaxbObject.setEnabled(data.isEnabled());
        return jaxbObject;
    }

    @Override
    public DataSourceNameOrId toJaxbNameOrId() {
        CalDataSourceNameOrId jaxbObject = CalDataSourceNameOrId.createForId(data.getId());
        return jaxbObject;
    }

    public ZJSONObject toZJSONObject() throws JSONException {
        ZJSONObject zjo = new ZJSONObject();
        zjo.put("id", data.getId());
        zjo.put("name", data.getName());
        zjo.put("enabled", data.isEnabled());
        zjo.put("folderId", data.getFolderId());
        return zjo;
    }
}
