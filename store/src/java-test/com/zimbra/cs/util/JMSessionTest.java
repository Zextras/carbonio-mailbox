// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.cs.util;

import java.util.HashMap;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.mail.smtp.SMTPMessage;
import com.zimbra.common.account.ZAttrProvisioning.ShareNotificationMtaConnectionType;
import com.zimbra.common.localconfig.LC;
import com.zimbra.common.mime.shim.JavaMailInternetAddress;
import com.zimbra.common.util.Log.Level;
import com.zimbra.common.util.ZimbraLog;
import com.zimbra.cs.account.Account;
import com.zimbra.cs.account.Domain;
import com.zimbra.cs.account.MockProvisioning;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.cs.account.Server;
import com.zimbra.cs.mailbox.MailboxTestUtil;
import com.zimbra.cs.mailclient.smtp.SmtpTransport;
import com.zimbra.cs.mailclient.smtp.SmtpsTransport;

/**
 * Unit test for {@link JMSession}.
 *
 * @author ysasaki
 */
public class JMSessionTest {

    @BeforeClass
    public static void init() throws Exception {
        MailboxTestUtil.initServer();
        LC.zimbra_attrs_directory.setDefault(MailboxTestUtil.getZimbraServerDir("") + "conf/attrs");
        MockProvisioning prov = new MockProvisioning();
        prov.getLocalServer().setSmtpPort(25);
        Provisioning.setInstance(prov);
    }

    @Test
    public void getTransport() throws Exception {
        Assert.assertSame(SmtpTransport.class,
                JMSession.getSession().getTransport("smtp").getClass());
        Assert.assertSame(SmtpsTransport.class,
                JMSession.getSession().getTransport("smtps").getClass());

        Assert.assertSame(SmtpTransport.class,
                JMSession.getSmtpSession().getTransport("smtp").getClass());
        Assert.assertSame(SmtpsTransport.class,
                JMSession.getSmtpSession().getTransport("smtps").getClass());
    }

    //@Test
    public void testRelayMta() throws Exception {
        Provisioning prov = Provisioning.getInstance();
        Server server = prov.getLocalServer();
        server.setShareNotificationMtaHostname("mta02.zimbra.com");
        server.setShareNotificationMtaPort(25);
        server.setShareNotificationMtaAuthRequired(true);
        server.setShareNotificationMtaConnectionType(ShareNotificationMtaConnectionType.STARTTLS);
        server.setShareNotificationMtaAuthAccount("test-jylee");
        server.setShareNotificationMtaAuthPassword("test123");

        SMTPMessage out = new SMTPMessage(JMSession.getRelaySession());
        InternetAddress address = new JavaMailInternetAddress("test-jylee@zimbra.com");
        out.setFrom(address);

        address = new JavaMailInternetAddress("test-jylee@zimbra.com");
        out.setRecipient(javax.mail.Message.RecipientType.TO, address);

        out.setSubject("test mail");
        out.setText("hello world");

        out.saveChanges();
        ZimbraLog.smtp.setLevel(Level.trace);
        Transport.send(out);
    }

    @Test
    public void messageID() throws Exception {
        Provisioning prov = Provisioning.getInstance();
        Domain domain = prov.createDomain("example.com", new HashMap<String, Object>());
        Account account = prov.createAccount("user1@example.com", "test123", new HashMap<String, Object>());

        MimeMessage mm = new MimeMessage(JMSession.getSmtpSession(account));
        mm.saveChanges();
        Assert.assertEquals("message ID contains account domain", domain.getName() + '>', mm.getMessageID().split("@")[1]);
    }
}
