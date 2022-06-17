// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.cs.servlet;

import com.zimbra.cs.account.Provisioning;
import com.zimbra.cs.service.MockHttpServletRequest;
import com.zimbra.cs.service.MockHttpServletResponse;
import java.net.URL;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ZimbraServletTest {

  private static String uri = "/Briefcase/上的发生的发";

  @Disabled("until bug 60345 is fixed")
  @Test
  public void proxyTest() throws Exception {
    MockHttpServletRequest req =
        new MockHttpServletRequest(
            "test".getBytes("UTF-8"), new URL("http://localhost:7070/user1" + uri), "");
    MockHttpServletResponse resp = new MockHttpServletResponse();
    ZimbraServlet.proxyServletRequest(
        req, resp, Provisioning.getInstance().getLocalServer(), uri, null);
  }
}
