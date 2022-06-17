// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.cs.index.query;

import com.zimbra.cs.index.ZimbraAnalyzer;
import com.zimbra.cs.mailbox.MailboxTestUtil;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link SubjectQuery}.
 *
 * @author ysasaki
 */
public final class SubjectQueryTest {

  @BeforeAll
  public static void init() throws Exception {
    MailboxTestUtil.initServer();
  }

  @Test
  public void emptySubject() throws Exception {
    Query query = SubjectQuery.create(ZimbraAnalyzer.getInstance(), "");
    Assert.assertEquals(TextQuery.class, query.getClass());
    Assert.assertEquals("Q(subject:)", query.toString());
  }
}
