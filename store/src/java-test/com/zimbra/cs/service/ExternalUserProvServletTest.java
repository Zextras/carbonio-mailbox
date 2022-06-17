// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.cs.service;

import com.google.common.collect.Maps;
import com.zimbra.common.account.Key;
import com.zimbra.common.account.ZAttrProvisioning.FeatureAddressVerificationStatus;
import com.zimbra.cs.account.Account;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.cs.mailbox.MailboxTestUtil;
import com.zimbra.cs.util.ZTestWatchman;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import junit.framework.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestName;

public class ExternalUserProvServletTest {
  @Rule public TestName testName = new TestName();
  @Rule public MethodRule watchman = new ZTestWatchman();

  @BeforeAll
  public static void init() throws Exception {
    MailboxTestUtil.initServer();
  }

  @BeforeEach
  public void before() throws Exception {
    System.out.println(testName.getMethodName());
    Provisioning prov = Provisioning.getInstance();

    Map<String, Object> attrs = Maps.newHashMap();
    attrs = Maps.newHashMap();
    prov.createAccount("test@zimbra.com", "secret", attrs);
  }

  @Test
  public void testHandleAddressVerificationExpired() throws Exception {
    Account acct1 = Provisioning.getInstance().get(Key.AccountBy.name, "test@zimbra.com");
    HashMap<String, String> headers = new HashMap<String, String>();
    HttpServletRequest req =
        new MockHttpServletRequest(null, null, null, 123, "127.0.0.1", headers);
    MockHttpServletResponse resp = new MockHttpServletResponse();
    ExternalUserProvServlet servlet = new ExternalUserProvServlet();
    servlet.handleAddressVerification(req, resp, acct1.getId(), "test2@zimbra.com", true);
    Assert.assertNull(acct1.getPrefMailForwardingAddress());
    Assert.assertEquals(
        FeatureAddressVerificationStatus.expired, acct1.getFeatureAddressVerificationStatus());
  }

  @Test
  public void testHandleAddressVerificationSuccess() throws Exception {
    Account acct1 = Provisioning.getInstance().get(Key.AccountBy.name, "test@zimbra.com");
    HashMap<String, String> headers = new HashMap<String, String>();
    HttpServletRequest req =
        new MockHttpServletRequest(null, null, null, 123, "127.0.0.1", headers);
    MockHttpServletResponse resp = new MockHttpServletResponse();
    ExternalUserProvServlet servlet = new ExternalUserProvServlet();
    servlet.handleAddressVerification(req, resp, acct1.getId(), "test2@zimbra.com", false);
    Assert.assertEquals("test2@zimbra.com", acct1.getPrefMailForwardingAddress());
    Assert.assertEquals(
        FeatureAddressVerificationStatus.verified, acct1.getFeatureAddressVerificationStatus());
  }

  @AfterEach
  public void tearDown() {
    try {
      MailboxTestUtil.clearData();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
