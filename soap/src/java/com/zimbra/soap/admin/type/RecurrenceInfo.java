// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.soap.admin.type;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import com.zimbra.common.soap.MailConstants;
import com.zimbra.soap.base.RecurRuleBaseInterface;
import com.zimbra.soap.base.RecurrenceInfoInterface;

@XmlAccessorType(XmlAccessType.NONE)
public class RecurrenceInfo
implements RecurRuleBase, RecurrenceInfoInterface {

    /**
     * @zm-api-field-description Recurrence rules
     */
    @XmlElements({
        @XmlElement(name=MailConstants.E_CAL_ADD /* add */, type=AddRecurrenceInfo.class),
        @XmlElement(name=MailConstants.E_CAL_EXCLUDE /* exclude */, type=ExcludeRecurrenceInfo.class),
        @XmlElement(name=MailConstants.E_CAL_EXCEPT /* except */, type=ExceptionRuleInfo.class),
        @XmlElement(name=MailConstants.E_CAL_CANCEL /* cancel */, type=CancelRuleInfo.class),
        @XmlElement(name=MailConstants.E_CAL_DATES /* dates */, type=SingleDates.class),
        @XmlElement(name=MailConstants.E_CAL_RULE /* rule */, type=SimpleRepeatingRule.class)
    })
    private List<RecurRuleBase> rules = Lists.newArrayList();

    public RecurrenceInfo() {
    }

    public void setRules(Iterable <RecurRuleBase> rules) {
        this.rules.clear();
        if (rules != null) {
            Iterables.addAll(this.rules,rules);
        }
    }

    public RecurrenceInfo addRule(RecurRuleBase rule) {
        this.rules.add(rule);
        return this;
    }

    public List<RecurRuleBase> getRules() {
        return Collections.unmodifiableList(rules);
    }

    @Override
    public void setRuleInterfaces(Iterable<RecurRuleBaseInterface> rules) {
        setRules(RecurrenceInfo.fromInterfaces(rules));
    }

    @Override
    public void addRuleInterface(RecurRuleBaseInterface rule) {
        addRule((RecurRuleBase) rule);
    }

    @Override
    public List<RecurRuleBaseInterface> getRuleInterfaces() {
        return RecurrenceInfo.toInterfaces(rules);
    }

    public static Iterable <RecurRuleBase> fromInterfaces(
                    Iterable <RecurRuleBaseInterface> params) {
        if (params == null)
            return null;
        List <RecurRuleBase> newList = Lists.newArrayList();
        for (RecurRuleBaseInterface param : params) {
            newList.add((RecurRuleBase) param);
        }
        return newList;
    }

    public static List <RecurRuleBaseInterface> toInterfaces(
            Iterable <RecurRuleBase> params) {
        if (params == null)
            return null;
        List <RecurRuleBaseInterface> newList = Lists.newArrayList();
        for (RecurRuleBase param : params) {
            newList.add((RecurRuleBaseInterface) param);
        }
        return newList;
    }

    public MoreObjects.ToStringHelper addToStringInfo(
                MoreObjects.ToStringHelper helper) {
        return helper
            .add("rules", rules);
    }

    @Override
    public String toString() {
        return addToStringInfo(MoreObjects.toStringHelper(this))
                .toString();
    }
}
