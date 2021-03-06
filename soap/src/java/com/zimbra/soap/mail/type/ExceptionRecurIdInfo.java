// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.soap.mail.type;

import com.google.common.base.MoreObjects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.zimbra.common.gql.GqlConstants;
import com.zimbra.common.soap.MailConstants;
import com.zimbra.soap.base.ExceptionRecurIdInfoInterface;

import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.GraphQLInputField;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;

// See ToXML.encodeRecurId

@XmlAccessorType(XmlAccessType.NONE)
@GraphQLType(name=GqlConstants.CLASS_EXCEPTION_RECURRENCE_ID_INFORMATION, description="Exception recurrence id information")
public class ExceptionRecurIdInfo
implements ExceptionRecurIdInfoInterface {

    /**
     * @zm-api-field-tag DATETIME-YYYYMMDD['T'HHMMSS[Z]]
     * @zm-api-field-description Date and/or time.  Format is : <b>YYYYMMDD['T'HHMMSS[Z]]</b>
     * <br />
     * where:
     * <pre>
     *     YYYY - 4 digit year
     *     MM   - 2 digit month
     *     DD   - 2 digit day
     * Optionally:
     *     'T' the literal char "T" then 
     *     HH - 2 digit hour (00-23)
     *     MM - 2 digit minute (00-59)
     *     SS - 2 digit second (00-59)
     *     ...and finally an optional "Z" meaning that the time is UTC,
     *     otherwise the tz="TIMEZONE" param MUST be specified with the DATETIME
     *     e.g:
     *         20050612  June 12, 2005
     *         20050315T18302305Z  March 15, 2005 6:30:23.05 PM UTC
     * </pre>
     */
    @XmlAttribute(name=MailConstants.A_CAL_DATETIME /* d */, required=true)
    @GraphQLQuery(name=GqlConstants.DATE_TIME, description="Date and/or time.  Format is : YYYYMMDD['T'HHMMSS[Z]]")
    private final String dateTime;

    /**
     * @zm-api-field-tag timezone-identifier
     * @zm-api-field-description Java timezone identifier
     */
    @XmlAttribute(name=MailConstants.A_CAL_TIMEZONE /* tz */, required=false)
    private String timezone;

    /**
     * @zm-api-field-tag range-type
     * @zm-api-field-description Range type - 1 means NONE, 2 means THISANDFUTURE, 3 means THISANDPRIOR
     */
    @XmlAttribute(name=MailConstants.A_CAL_RECURRENCE_RANGE_TYPE /* rangeType */, required=false)
    private Integer recurrenceRangeType;

    /**
     * no-argument constructor wanted by JAXB
     */
    @SuppressWarnings("unused")
    private ExceptionRecurIdInfo() {
        this((String) null);
    }

    public ExceptionRecurIdInfo(
        @GraphQLNonNull @GraphQLInputField(name=GqlConstants.DATE_TIME) String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    @GraphQLIgnore
    public ExceptionRecurIdInfoInterface create(String dateTime) {
        return new ExceptionRecurIdInfo(dateTime);
    }

    @Override
    @GraphQLInputField(name=GqlConstants.TIMEZONE, description="Java timezone identifier")
    public void setTimezone(String timezone) { this.timezone = timezone; }
    @Override
    @GraphQLInputField(name=GqlConstants.RECURRENCE_RANGE_TYPE, description="Range type\n "
        + "* 1: NONE\n "
        + "* 2: THISANDFUTURE\n "
        + "* 3: THISANDPRIOR")
    public void setRecurrenceRangeType(Integer recurrenceRangeType) {
        this.recurrenceRangeType = recurrenceRangeType;
    }

    @Override
    public String getDateTime() { return dateTime; }
    @Override
    @GraphQLQuery(name=GqlConstants.TIMEZONE, description="Java timezone identifier")
    public String getTimezone() { return timezone; }
    @Override
    @GraphQLQuery(name=GqlConstants.RECURRENCE_RANGE_TYPE, description="Range type\n "
        + "* 1: NONE\n "
        + "* 2: THISANDFUTURE\n "
        + "* 3: THISANDPRIOR")
    public Integer getRecurrenceRangeType() { return recurrenceRangeType; }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("dateTime", dateTime)
            .add("timezone", timezone)
            .add("recurrenceRangeType", recurrenceRangeType)
            .toString();
    }
}
