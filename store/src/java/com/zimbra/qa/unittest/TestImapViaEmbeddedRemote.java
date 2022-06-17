// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: AGPL-3.0-only

package com.zimbra.qa.unittest;

import com.zimbra.common.localconfig.ConfigException;
import com.zimbra.common.localconfig.LC;
import com.zimbra.common.service.ServiceException;
import java.io.IOException;
import org.dom4j.DocumentException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Test the Embedded Remote IMAP server, doing the necessary configuration to make it work.
 *
 * <p>Note: Currently bypasses Proxy, the tests connect directly to the embedded IMAP server's port
 *
 * <p>The actual tests that are run are in {@link SharedImapTests}
 */
public class TestImapViaEmbeddedRemote extends SharedImapTests {

  @BeforeEach
  public void setUp() throws ServiceException, IOException, DocumentException, ConfigException {
    TestUtil.setLCValue(LC.imap_always_use_remote_store, String.valueOf(true));
    imapServer.setReverseProxyUpstreamImapServers(new String[] {});
    super.sharedSetUp();
    TestUtil.assumeTrue(
        "embedded remote IMAP server is not enabled", imapServer.isImapServerEnabled());
  }

  @AfterEach
  public void tearDown() throws ServiceException, DocumentException, ConfigException, IOException {
    super.sharedTearDown();
  }

  @Override
  protected int getImapPort() {
    return imapServer.getImapBindPort();
  }
}
