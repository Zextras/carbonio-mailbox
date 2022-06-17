// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: AGPL-3.0-only

package com.zimbra.qa.unittest;

import static org.junit.Assert.assertTrue;

import com.zimbra.common.localconfig.ConfigException;
import com.zimbra.common.localconfig.LC;
import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.account.Account;
import com.zimbra.cs.account.Provisioning;
import java.io.IOException;
import org.dom4j.DocumentException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * This is a shell test for Local IMAP tests that does the necessary configuration to select the
 * local variant of IMAP access.
 *
 * <p>Note: Currently bypasses Proxy, the tests connect directly to the embedded IMAP server's port
 *
 * <p>The actual tests that are run are in {@link SharedImapTests}
 */
public class TestImapViaEmbeddedLocal extends SharedImapTests {

  @BeforeEach
  public void setUp() throws ServiceException, IOException, DocumentException, ConfigException {
    TestUtil.setLCValue(LC.imap_always_use_remote_store, String.valueOf(false));
    imapServer.setReverseProxyUpstreamImapServers(new String[] {});
    super.sharedSetUp();
    TestUtil.assumeTrue("local IMAP server is not enabled", imapServer.isImapServerEnabled());
  }

  @AfterEach
  public void tearDown() throws ServiceException, DocumentException, ConfigException, IOException {
    super.sharedTearDown();
  }

  @Override
  protected int getImapPort() {
    return imapServer.getImapBindPort();
  }

  @Test
  @Timeout(10000)
  public void testReallyUsingLocalImap() throws ServiceException {
    Account uAcct = TestUtil.getAccount(USER);
    assertTrue("Provisioning.canUseLocalIMAP(" + USER + ")", Provisioning.canUseLocalIMAP(uAcct));
  }
}
