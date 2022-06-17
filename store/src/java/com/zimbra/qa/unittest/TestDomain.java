// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.qa.unittest;

import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.account.AccountServiceException;
import com.zimbra.cs.account.Domain;
import com.zimbra.cs.account.Provisioning;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestDomain extends TestCase {
  private Provisioning mProv = Provisioning.getInstance();
  private String DOMAIN_NAME = "testautporov.domain.com";
  Domain domain = null;

  @Override
  @BeforeEach
  public void setUp() throws Exception {
    // create domain
    Map<String, Object> attrs = new HashMap<String, Object>();
    domain = mProv.createDomain(DOMAIN_NAME, attrs);
    assertNotNull(domain);
  }

  @Override
  @AfterEach
  public void tearDown() throws Exception {
    if (domain != null) {
      mProv.deleteDomain(domain.getId());
    }
  }

  @Test
  public void testModifyAutoProvTimeStamp() {
    try {
      domain.setAutoProvLastPolledTimestampAsString("20140328000454.349Z");
      assertEquals(
          "domain has incorrect last prov time stamp",
          "20140328000454.349Z",
          domain.getAutoProvLastPolledTimestampAsString());

      domain.setAutoProvLastPolledTimestampAsString("20140328000454Z");
      assertEquals(
          "domain has incorrect last prov time stamp",
          "20140328000454Z",
          domain.getAutoProvLastPolledTimestampAsString());

      domain.setAutoProvLastPolledTimestampAsString("20140328000454.0Z");
      assertEquals(
          "domain has incorrect last prov time stamp",
          "20140328000454.0Z",
          domain.getAutoProvLastPolledTimestampAsString());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }
    try {
      domain.setAutoProvLastPolledTimestampAsString("20140328000454.123456Z");
      fail("invalid date-time format should cause an exception");
    } catch (ServiceException e) {
      // should throw an exception
      assertEquals(
          "catching a wrong exception: " + e.getMessage(),
          e.getCode(),
          AccountServiceException.INVALID_ATTR_VALUE);
    }
  }
}
