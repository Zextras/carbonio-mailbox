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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.zimbra.common.soap.MailConstants;
import com.zimbra.soap.mail.type.IdsAttr;
import com.zimbra.soap.type.NamedElement;

/**
 * @zm-api-command-auth-required true
 * @zm-api-command-admin-auth-required false
 * @zm-api-command-description Applies one or more filter rules to messages specified by a comma-separated ID list,
 * or returned by a search query.  One or the other can be specified, but not both.  Returns the list of ids of
 * existing messages that were affected.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name=MailConstants.E_APPLY_OUTGOING_FILTER_RULES_REQUEST)
public class ApplyOutgoingFilterRulesRequest {

    /**
     * @zm-api-field-description Filter rules
     */
    @XmlElementWrapper(name=MailConstants.E_FILTER_RULES /* filterRules */, required=true)
    @XmlElement(name=MailConstants.E_FILTER_RULE /* filterRule */, required=false)
    private List<NamedElement> filterRules = Lists.newArrayList();

    /**
     * @zm-api-field-tag comma-sep-msg-ids
     * @zm-api-field-description Comma-separated list of message IDs
     */
    @XmlElement(name=MailConstants.E_MSG /* m */, required=false)
    private IdsAttr msgIds;

    /**
     * @zm-api-field-tag query-string
     * @zm-api-field-description Query string
     */
    @XmlElement(name=MailConstants.E_QUERY /* query */, required=false)
    private String query;

    public ApplyOutgoingFilterRulesRequest() {
    }

    public void setFilterRules(Iterable <NamedElement> filterRules) {
        this.filterRules.clear();
        if (filterRules != null) {
            Iterables.addAll(this.filterRules,filterRules);
        }
    }

    public ApplyOutgoingFilterRulesRequest addFilterRule(
                        NamedElement filterRule) {
        this.filterRules.add(filterRule);
        return this;
    }

    public void setMsgIds(IdsAttr msgIds) { this.msgIds = msgIds; }
    public void setQuery(String query) { this.query = query; }
    public List<NamedElement> getFilterRules() {
        return Collections.unmodifiableList(filterRules);
    }
    public IdsAttr getMsgIds() { return msgIds; }
    public String getQuery() { return query; }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("filterRules", filterRules)
            .add("msgIds", msgIds)
            .add("query", query)
            .toString();
    }
}
