// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.soap.mail.type;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.zimbra.common.gql.GqlConstants;
import com.zimbra.common.soap.MailConstants;
import com.zimbra.soap.base.DtValInterface;
import com.zimbra.soap.base.SingleDatesInterface;

import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.GraphQLInputField;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;

@XmlAccessorType(XmlAccessType.NONE)
@GraphQLType(name=GqlConstants.CLASS_SINGLE_DATES, description="Single dates information")
public class SingleDates
implements RecurRuleBase, SingleDatesInterface {

    /**
     * @zm-api-field-tag TZID
     * @zm-api-field-description TZID
     */
    @XmlAttribute(name=MailConstants.A_CAL_TIMEZONE /* tz */, required=false)
    private String timezone;

    /**
     * @zm-api-field-description Information on start date/time and end date/time or duration
     */
    @XmlElement(name=MailConstants.E_CAL_DATE_VAL /* dtval */, required=false)
    @GraphQLIgnore
    private List<DtVal> dtVals = Lists.newArrayList();

    public SingleDates() {
    }

    @GraphQLInputField(name=GqlConstants.TIMEZONE, description="TZID")
    public void setTimezone(String timezone) { this.timezone = timezone; }
    @GraphQLInputField(name=GqlConstants.DATE_TIME_VALUES, description="Information on start date/time and end date/time or duration")
    public void setDtvals(Iterable <DtVal> dtVals) {
        this.dtVals.clear();
        if (dtVals != null) {
            Iterables.addAll(this.dtVals,dtVals);
        }
    }

    @GraphQLIgnore
    public void addDtval(DtVal dtVal) {
        this.dtVals.add(dtVal);
    }

    @GraphQLQuery(name=GqlConstants.TIMEZONE, description="TZID")
    public String getTimezone() { return timezone; }
    @GraphQLQuery(name=GqlConstants.DATE_TIME_VALUES, description="Information on start date/time and end date/time or duration")
    public List<DtVal> getDtvals() {
        return dtVals;
    }

    @Override
    @GraphQLIgnore
    public void setDtValInterfaces(Iterable<DtValInterface> dtVals) {
        setDtvals(DtVal.fromInterfaces(dtVals));
    }

    @Override
    @GraphQLIgnore
    public void addDtValInterface(DtValInterface dtVal) {
        addDtval((DtVal) dtVal);
    }

    @Override
    @GraphQLIgnore
    public List<DtValInterface> getDtValInterfaces() {
        return DtVal.toInterfaces(dtVals);
    }

    public MoreObjects.ToStringHelper addToStringInfo(MoreObjects.ToStringHelper helper) {
        return helper
            .add("timezone", timezone)
            .add("dtVals", dtVals);
    }

    @Override
    public String toString() {
        return addToStringInfo(MoreObjects.toStringHelper(this)).toString();
    }
}
