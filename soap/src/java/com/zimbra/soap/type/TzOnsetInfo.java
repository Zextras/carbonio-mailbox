// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.soap.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.zimbra.common.gql.GqlConstants;
import com.zimbra.common.soap.MailConstants;

import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;

@XmlAccessorType(XmlAccessType.NONE)
@GraphQLType(name=GqlConstants.CLASS_TZ_ONSET_INFO, description="Time/rule for transitioning from daylight time to standard time. Either specify week/wkday combo, or mday.")
public class TzOnsetInfo {

    /**
     * @zm-api-field-tag tzonset-week
     * @zm-api-field-description Week number; 1=first, 2=second, 3=third, 4=fourth, -1=last
     */
    @XmlAttribute(name=MailConstants.A_CAL_TZ_WEEK /* week */, required=false)
    @GraphQLQuery(name=GqlConstants.WEEK, description="Week number; 1=first, 2=second, 3=third, 4=fourth, -1=last")
    private Integer week;

    /**
     * @zm-api-field-tag tzonset-day-of-week
     * @zm-api-field-description Day of week; 1=Sunday, 2=Monday, etc.
     */
    @XmlAttribute(name=MailConstants.A_CAL_TZ_DAYOFWEEK /* wkday */, required=false)
    @GraphQLQuery(name=GqlConstants.DAY_OF_WEEK, description="Day of week; 1=Sunday, 2=Monday, etc.")
    private Integer dayOfWeek;

    /**
     * @zm-api-field-tag tzonset-month
     * @zm-api-field-description Month; 1=January, 2=February, etc.
     */
    @XmlAttribute(name=MailConstants.A_CAL_TZ_MONTH /* mon */, required=true)
    @GraphQLNonNull
    @GraphQLQuery(name=GqlConstants.MONTH, description="Month; 1=January, 2=February, etc.")
    private Integer month;

    /**
     * @zm-api-field-tag tzonset-day-of-month
     * @zm-api-field-description Day of month (1..31)
     */
    @XmlAttribute(name=MailConstants.A_CAL_TZ_DAYOFMONTH /* mday */, required=false)
    @GraphQLQuery(name=GqlConstants.DAY_OF_MONTH, description="Day of month (1..31)")
    private Integer dayOfMonth;

    /**
     * @zm-api-field-tag tzonset-hour
     * @zm-api-field-description Transition hour (0..23)
     */
    @XmlAttribute(name=MailConstants.A_CAL_TZ_HOUR /* hour */, required=true)
    @GraphQLNonNull
    @GraphQLQuery(name=GqlConstants.HOUR, description="Transition hour (0..23)")
    private Integer hour;

    /**
     * @zm-api-field-tag tzonset-minute
     * @zm-api-field-description Transition minute (0..59)
     */
    @XmlAttribute(name=MailConstants.A_CAL_TZ_MINUTE /* min */, required=true)
    @GraphQLNonNull
    @GraphQLQuery(name=GqlConstants.MINUTE, description="Transition minute (0..59)")
    private Integer minute;

    /**
     * @zm-api-field-tag tzonset-second
     * @zm-api-field-description Transition second; 0..59, usually 0
     */
    @XmlAttribute(name=MailConstants.A_CAL_TZ_SECOND /* sec */, required=true)
    @GraphQLNonNull
    @GraphQLQuery(name=GqlConstants.SECOND, description="Transition second; 0..59, usually 0")
    private Integer second;

    public TzOnsetInfo() {
    }

    public void setWeek(Integer week) { this.week = week; }
    public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public void setMonth(@GraphQLNonNull Integer month) { this.month = month; }
    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setHour(@GraphQLNonNull Integer hour) { this.hour = hour; }
    public void setMinute(@GraphQLNonNull Integer minute) { this.minute = minute; }
    public void setSecond(@GraphQLNonNull Integer second) { this.second = second; }
    public Integer getWeek() { return week; }
    public Integer getDayOfWeek() { return dayOfWeek; }
    public Integer getMonth() { return month; }
    public Integer getDayOfMonth() { return dayOfMonth; }
    public Integer getHour() { return hour; }
    public Integer getMinute() { return minute; }
    public Integer getSecond() { return second; }
}
