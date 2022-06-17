// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: AGPL-3.0-only

package com.zimbra.cs.redolog.logger;

import static org.junit.jupiter.api.Assertions.*;

import com.zimbra.cs.mailbox.MailboxOperation;
import com.zimbra.cs.mailbox.MailboxTestUtil;
import com.zimbra.cs.redolog.RedoLogManager;
import com.zimbra.cs.redolog.op.RedoableOp;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import junit.framework.Assert;
import org.easymock.EasyMock;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;

public class FileLogWriterTest {
  @Rule public TemporaryFolder folder = new TemporaryFolder();

  private RedoLogManager mockRedoLogManager;
  private FileLogWriter logWriter;

  @BeforeAll
  public static void init() throws Exception {
    MailboxTestUtil.initServer();
  }

  @BeforeEach
  public void setUp() throws Exception {
    mockRedoLogManager = EasyMock.createNiceMock(RedoLogManager.class);

    logWriter =
        new FileLogWriter(
            mockRedoLogManager, folder.newFile("logfile"), 10 /* fsync interval in ms */);
  }

  @Test
  public void openLogClose() throws Exception {
    Assert.assertTrue("file starts empty", logWriter.isEmpty());
    logWriter.open();
    Assert.assertTrue("file empty after open", logWriter.isEmpty());

    RedoableOp op =
        EasyMock.createMockBuilder(RedoableOp.class)
            .withConstructor(MailboxOperation.Preview)
            .createMock();

    logWriter.log(op, new ByteArrayInputStream("some bytes".getBytes()), false /* asynchronous */);
    // The file is the size of the header plus the op bytes (10)
    Assert.assertEquals("file size incorrect.", FileHeader.HEADER_LEN + 10, logWriter.getSize());
    logWriter.close();
    // store some fields from the current writer.
    final long createTime = logWriter.getCreateTime();
    final long sequence = logWriter.getSequence();

    // reset the FileLogWriter
    logWriter =
        new FileLogWriter(
            mockRedoLogManager, folder.newFile("logfile"), 10 /* fsync interval in ms */);
    Assert.assertEquals("file size incorrect.", FileHeader.HEADER_LEN + 10, logWriter.getSize());

    logWriter.open();
    Assert.assertEquals(createTime, logWriter.getCreateTime());
    Assert.assertEquals(sequence, logWriter.getSequence());
  }

  @Test
  public void logBeforeOpen() throws Exception {
    assertThrows(IOException.class, () -> logWriter.log(null, null, false));
  }
}
