// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.client;

import org.json.JSONException;

import com.zimbra.common.util.SystemUtil;
import com.zimbra.soap.admin.type.DataSourceType;
import com.zimbra.soap.mail.type.DataSourceNameOrId;
import com.zimbra.soap.mail.type.MailDataSource;
import com.zimbra.soap.mail.type.MailUnknownDataSource;
import com.zimbra.soap.type.DataSource;
import com.zimbra.soap.type.DataSources;

public class ZDataSource implements ToZJSONObject {
    protected DataSource data;
    public static String SOURCE_HOST_YAHOO = "yahoo.com";

    public ZDataSource() {
        data = DataSources.newDataSource();
        data.setEnabled(false);
    }

    public ZDataSource(String name, boolean enabled) {
        data = DataSources.newDataSource();
        data.setName(name);
        data.setEnabled(enabled);
    }

    public ZDataSource(String name, boolean enabled, Iterable<String> attributes) {
        data = DataSources.newDataSource();
        data.setAttributes(attributes);
        data.setName(name);
        data.setEnabled(enabled);
    }

    public ZDataSource(DataSource data) {
        this.data = DataSources.newDataSource(data);
    }

    public DataSource toJaxb() {
        MailDataSource jaxbObject = new MailDataSource();
        jaxbObject.setId(data.getId());
        jaxbObject.setName(data.getName());
        jaxbObject.setEnabled(data.isEnabled());
        jaxbObject.setFolderId(data.getFolderId());
        jaxbObject.setRefreshToken(data.getRefreshToken());
        jaxbObject.setRefreshTokenUrl(data.getRefreshTokenUrl());
        jaxbObject.setImportOnly(data.isImportOnly());
        jaxbObject.setImportClass(data.getImportClass());
        jaxbObject.setHost(data.getHost());
        return jaxbObject;
    }

    public DataSourceNameOrId toJaxbNameOrId() {
        DataSourceNameOrId jaxbObject = DataSourceNameOrId.createForId(data.getId());
        return jaxbObject;
    }

    @Override
    public ZJSONObject toZJSONObject() throws JSONException {
        ZJSONObject zjo = new ZJSONObject();
        zjo.put("id", data.getId());
        zjo.put("name", data.getName());
        zjo.put("enabled", data.isEnabled());
        zjo.put("folderId", data.getFolderId());
        zjo.put("refreshToken", data.getRefreshToken());
        zjo.put("refreshTokenUrl", data.getRefreshTokenUrl());
        zjo.put("importOnly", data.isImportOnly());
        zjo.put("importClass", data.getImportClass());
        zjo.put("host", data.getHost());
        return zjo;
    }

    public String getName() {
        return data.getName();
    }
    public void setName(String name) {
        data.setName(name);
    }
    public DataSourceType getType() {
        return DataSourceType.unknown;
    }
    public String getId() {
        return data.getId();
    }
    public void setId(String id) {
        data.setId(id);
    }
    public String getFolderId() { return data.getFolderId(); }
    public void setFolderId(String folderid) {
        data.setFolderId(folderid);
    }
    public void setRefreshToken(String val) {
        data.setRefreshToken(val);
    }
    public void setRefreshTokenURL(String val) {
        data.setRefreshTokenUrl(val);
    }
    public String getRefreshToken() {
        return data.getRefreshToken();
    }
    public String getRefreshTokenUrl() {
        return data.getRefreshTokenUrl();
    }
    public String getImportClass() {
        return data.getImportClass();
    }
    public void setImportClass(String importClass) {
        data.setImportClass(importClass);
    }
    public String getHost() {
        return data.getHost();
    }
    public void setHost(String host) {
        data.setHost(host);
    }
    public void setEnabled(boolean enabled) { data.setEnabled(enabled); }
    public boolean isEnabled() {
        return SystemUtil.coalesce(data.isEnabled(), Boolean.FALSE);
    }
}
