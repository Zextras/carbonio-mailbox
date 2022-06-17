// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.qa.unittest.server;

import com.zimbra.common.localconfig.LC;
import com.zimbra.cs.mailbox.Document;
import com.zimbra.cs.mailbox.MailItem;
import com.zimbra.cs.mailbox.Mailbox;
import com.zimbra.cs.mailbox.WikiItem;
import com.zimbra.cs.mime.ParsedDocument;
import com.zimbra.cs.store.MailboxBlob;
import com.zimbra.cs.store.StoreManager;
import com.zimbra.cs.volume.Volume;
import com.zimbra.cs.volume.VolumeManager;
import com.zimbra.qa.unittest.TestUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TestName;

public final class TestDocumentServer {
  @Rule public TestName testInfo = new TestName();
  private static String USER_NAME = null;
  private static final String NAME_PREFIX = TestDocumentServer.class.getSimpleName();
  private long mOriginalCompressionThreshold;
  private boolean mOriginalCompressBlobs;

  @BeforeEach
  public void setUp() throws Exception {
    String prefix = NAME_PREFIX + "-" + testInfo.getMethodName() + "-";
    USER_NAME = prefix + "user";
    cleanUp();
    TestUtil.createAccount(USER_NAME);
    Volume vol = VolumeManager.getInstance().getCurrentMessageVolume();
    mOriginalCompressBlobs = vol.isCompressBlobs();
    mOriginalCompressionThreshold = vol.getCompressionThreshold();
  }

  /**
   * Server-side test that confirms that all blobs are cleaned up when a document with multiple
   * revisions is deleted.
   */
  @Test
  public void testDeleteRevisions() throws Exception {
    Mailbox mbox = TestUtil.getMailbox(USER_NAME);
    // Create first revision.
    String content = "one";
    ParsedDocument pd =
        new ParsedDocument(
            new ByteArrayInputStream(content.getBytes()),
            NAME_PREFIX + "-testDeleteRevisions.txt",
            "text/plain",
            System.currentTimeMillis(),
            USER_NAME,
            "one",
            true);
    Document doc =
        mbox.createDocument(null, Mailbox.ID_FOLDER_BRIEFCASE, pd, MailItem.Type.DOCUMENT, 0);
    int docId = doc.getId();
    MailItem.Type type = doc.getType();
    File blobDir = getBlobDir(doc);
    List<Document> revisions = mbox.getAllRevisions(null, docId, type);
    Assert.assertEquals(1, revisions.size());
    if (TestUtil.checkLocalBlobs()) {
      Assert.assertEquals(1, getBlobCount(blobDir, docId));
    }
    Assert.assertEquals(true, doc.isDescriptionEnabled());
    // Add a second revision.
    content = "two";
    pd =
        new ParsedDocument(
            new ByteArrayInputStream(content.getBytes()),
            NAME_PREFIX + "-testDeleteRevisions2.txt",
            "text/plain",
            System.currentTimeMillis(),
            USER_NAME,
            "two",
            false);
    doc = mbox.addDocumentRevision(null, docId, pd);
    Assert.assertEquals(2, mbox.getAllRevisions(null, docId, type).size());
    if (TestUtil.checkLocalBlobs()) {
      Assert.assertEquals(2, getBlobCount(blobDir, docId));
    }
    Assert.assertEquals(false, doc.isDescriptionEnabled());
    // Move to trash, empty trash, and confirm that both blobs were deleted.
    mbox.move(null, doc.getId(), doc.getType(), Mailbox.ID_FOLDER_TRASH);
    mbox.emptyFolder(null, Mailbox.ID_FOLDER_TRASH, false);
    mbox.emptyDumpster(null);
    if (TestUtil.checkLocalBlobs()) {
      Assert.assertEquals(0, getBlobCount(blobDir, docId));
    }
  }

  private int getBlobCount(File dir, int id) throws Exception {
    int count = 0;
    String prefix = id + "-";
    for (File file : dir.listFiles()) {
      if (file.getName().startsWith(prefix)) {
        count++;
      }
    }
    return count;
  }

  private File getBlobDir(Document doc) throws Exception {
    MailboxBlob mblob = StoreManager.getInstance().getMailboxBlob(doc);
    File blobFile = mblob.getLocalBlob().getFile();
    return blobFile.getParentFile();
  }

  /** Confirms that saving a document to a compressed volume works correctly (bug 48363). */
  @Test
  public void testCompressedVolume() throws Exception {
    // Perform this test only if instant parsing is enabled.
    // Normally the instant parsing is enabled for ZCS and disabled for Octopus.
    if (LC.documents_disable_instant_parsing.booleanValue() == true) return;
    VolumeManager mgr = VolumeManager.getInstance();
    Volume current = mgr.getCurrentMessageVolume();
    mgr.update(Volume.builder(current).setCompressBlobs(true).setCompressionThreshold(1).build());
    String content =
        "<wiklet class='TOC' format=\"template\" bodyTemplate=\"_TocBodyTemplate\""
            + " itemTemplate=\"_TocItemTemplate\">abc</wiklet>";
    Mailbox mbox = TestUtil.getMailbox(USER_NAME);
    WikiItem wiki =
        mbox.createWiki(
            null,
            Mailbox.ID_FOLDER_BRIEFCASE,
            NAME_PREFIX + "-testCompressedVolume",
            "Unit Test",
            null,
            new ByteArrayInputStream(content.getBytes()));
    Assert.assertEquals("abc", wiki.getFragment());
  }

  @AfterEach
  public void tearDown() throws Exception {
    // Delete documents.
    Mailbox mbox = TestUtil.getMailbox(USER_NAME);
    for (MailItem item : mbox.getItemList(null, MailItem.Type.DOCUMENT)) {
      if (item.getName().contains(NAME_PREFIX)) {
        mbox.delete(null, item.getId(), item.getType());
      }
    }
    cleanUp();
    TestUtil.deleteAccountIfExists(USER_NAME);
  }

  private void cleanUp() throws Exception {
    // Restore volume compression settings.
    VolumeManager mgr = VolumeManager.getInstance();
    Volume current = mgr.getCurrentMessageVolume();
    Volume vol =
        Volume.builder(current)
            .setCompressBlobs(mOriginalCompressBlobs)
            .setCompressionThreshold(mOriginalCompressionThreshold)
            .build();
    mgr.update(vol);
  }
}
