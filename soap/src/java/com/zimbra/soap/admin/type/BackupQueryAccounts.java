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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.zimbra.common.soap.BackupConstants;
import com.zimbra.soap.type.ZmBoolean;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = {})
public class BackupQueryAccounts {

    /**
     * @zm-api-field-tag total-accts-in-backup-set
     * @zm-api-field-description Total number of accounts in backup set
     */
    @XmlAttribute(name=BackupConstants.A_TOTAL_COUNT /* total */, required=false)
    private Integer totalCount;

    /**
     * @zm-api-field-tag num-accts-whose-backups-completed-successfully
     * @zm-api-field-description Number of accounts whose backup completed successfully
     */
    @XmlAttribute(name=BackupConstants.A_COMPLETION_COUNT /* completionCount */, required=false)
    private Integer completionCount;

    /**
     * @zm-api-field-tag num-accts-with-errors
     * @zm-api-field-description Number of accounts that had error during backup
     */
    @XmlAttribute(name=BackupConstants.A_ERROR_COUNT /* errorCount */, required=false)
    private Integer errorCount;

    /**
     * @zm-api-field-tag more-flag
     * @zm-api-field-description Present if there are more accounts to page through
     */
    @XmlAttribute(name=BackupConstants.A_MORE /* more */, required=false)
    private ZmBoolean more;

    /**
     * @zm-api-field-description Account list returned if request specified accountListStatus
     */
    @XmlElement(name=BackupConstants.E_ACCOUNT /* account */, required=false)
    private List<BackupQueryAccountStatus> accounts = Lists.newArrayList();

    public BackupQueryAccounts() {
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
    public void setCompletionCount(Integer completionCount) {
        this.completionCount = completionCount;
    }
    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }
    public void setMore(Boolean more) { this.more = ZmBoolean.fromBool(more); }
    public void setAccounts(Iterable <BackupQueryAccountStatus> accounts) {
        this.accounts.clear();
        if (accounts != null) {
            Iterables.addAll(this.accounts,accounts);
        }
    }

    public void addAccount(BackupQueryAccountStatus account) {
        this.accounts.add(account);
    }

    public Integer getTotalCount() { return totalCount; }
    public Integer getCompletionCount() { return completionCount; }
    public Integer getErrorCount() { return errorCount; }
    public Boolean getMore() { return ZmBoolean.toBool(more); }
    public List<BackupQueryAccountStatus> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public MoreObjects.ToStringHelper addToStringInfo(
                MoreObjects.ToStringHelper helper) {
        return helper
            .add("totalCount", totalCount)
            .add("completionCount", completionCount)
            .add("errorCount", errorCount)
            .add("more", more)
            .add("accounts", accounts);
    }

    @Override
    public String toString() {
        return addToStringInfo(MoreObjects.toStringHelper(this))
                .toString();
    }
}
