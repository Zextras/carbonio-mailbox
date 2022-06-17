// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.qa.unittest.prov.ldap;

import com.zimbra.cs.account.Domain;
import com.zimbra.cs.account.Provisioning;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestLdapProvMisc extends LdapTest {

  private static LdapProvTestUtil provUtil;
  private static Provisioning prov;
  private static Domain domain;

  @BeforeAll
  public static void init() throws Exception {
    provUtil = new LdapProvTestUtil();
    prov = provUtil.getProv();
    domain = provUtil.createDomain(baseDomainName(), null);
  }

  @AfterAll
  public static void cleanup() throws Exception {
    Cleanup.deleteAll(baseDomainName());
  }

  @Test
  public void healthCheck() throws Exception {
    prov.healthCheck();
  }
}
