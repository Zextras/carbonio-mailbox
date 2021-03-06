Notes on Junk/Not Junk
======================

When user clicks on the Junk/Not Junk buttons in the Zimbra Web
Client, the selected messages and conversations are moved to Junk or
Inbox folders, as appropriate.  The SOAP request issued by the web
client is {Msg,Conv}ActionRequest with action op set to `spam` or
`!spam`.

In addition to the folder move, you can configure the Zimbra server to
fork a copy of the message on which the spam/!spam action was
performed, for instance, you want to feedback these user actions into
your spam system.

Overview
--------

- Admin creates Junk/Not Junk feedback receiving accounts, and changes
  global config to make the system aware that feedback is enabled.

- User marks message A as Junk.

- Server moves message A from current foolder to Junk folder.

- Server notices that feedback is enabled, and sends out a feedback
  message B to the configured feedback account.  Note that the content
  of message A is a `message/rfc822` attachment inside B.

- Admin runs a periodic task (cron job) to drain the feedback accounts
  with the zmspamextract utility, and then uses sa-learn (in the case
  of SpamAssassin) to train SpamAssassin's bayesian filter.

Notes on terminology:

- In this document, spam and ham are used interchangeable with Junk
  and Not Junk respectively.

- "Original message" is the message in the user's mailbox on which the
  user acted (message A in above example).  

- "Feedback message" is a new message sent to a configured address
  (message B in the example above) and it encapsulates the original
  message as an `message/rfc822` attachment.  B contains A.

Junk/Not Junk Accounts
----------------------

If global config attributes

  - zimbraSpamIsSpamAccount
  - zimbraSpamIsNotSpamAccount

are configured (valid config value is an email address), then a
feedback message is sent to these addresses on user action spam/!spam.
Junk feedback is sent to the former and Not Junk to the latter - you
can choose to have only one of them configured.

Most installs will create accounts in the Zimbra system to receive
spam feedback messages.  When creating these accounts, we recommend:

  - Setting the quota high or disabling quota on these accounts.  When
    the feedback message is sent the envelope MAIL FROM is set to <>,
    so there are no bounce messages generated if this mailbox is full.
    Make sure there are processes that will drain the account though.

  - Disable attachment indexing for these accounts because they are
    temporary holding places.  It would be a waste of resources to
    index these mailboxes.

  - Disable  spam  checking on  these  accounts.
  
For example:

    $ domain=`zmprov gcf zimbraDefaultDomainName | sed 's/^.*: //g'`
    $ rand_name=`zmjava com.zimbra.common.util.RandomPassword 8 10 | sed 's/\.//g'`
    $ rand_passwd=`zmjava com.zimbra.common.util.RandomPassword 31 37`
    $ echo "Creating account $rand_name@$domain with passwd=$rand_passwd"
    $ zmprov ca $rand_name@$domain $rand_passwd \
             zimbraAttachmentsIndexingEnabled FALSE \
             zimbraMailQuota 0 \
             amavisBypassSpamChecks TRUE
    $ zmprov mcf zimbraSpamIsSpamAccount $rand_name@$domain

The zimbra installer creates these accounts for you by default.

Note on Server Implementation
-----------------------------

On the server side, the folder move is done inline with the SOAP call,
but the feedback send is processed out of band of the SOAP call and
handled by a queue.  The queue is processed by a long lived thread
dedicated to sending this junk/not junk feedback.  The queue size is
throttled so if this thread falls behind we choose to ignore sending
the feedback (warnigs are logged when queue if full and feedback
sending is ignored).  You can change queue size in server config
(`zimbraMailboxSpamHandlerSpamReportQueueSize`, defaulted to 100).  Also the feedback
queue is not persisted - it is in memory only, and does not survive
server restarts.

Format of Feedback Message
--------------------------

The feedback message has a text-plain part and a `message/rfc822`
attachment which is the original message. In the example below, the
(1) is the envelope of the feedback message, (2) is a spam report
detailing who classified the message as junk or not junk, and (3) is
the original message.

    1  Received: from ...
    1  Message-ID: <26499674.231142039856777.JavaMail@phillip.liquidsys.com>
    1  To: spam-sink@phillip.liquidsys.com
    1  Subject: spam-report: user1@phillip.liquidsys.com: spam
    1  MIME-Version: 1.0
    1  Content-Type: multipart/mixed; boundary="----=_Part_23_13878940.114203"
    1  Date: Fri, 10 Mar 2006 17:17:36 -0800 (PST)
    1  From: MAILER-DAEMON

       ------=_Part_23_13878940.114203
    2  Content-Type: text/plain; charset=us-ascii
    2  Content-Transfer-Encoding: 7bit
    2  Content-Description: Zimbra spam classification report
    2  
    2  Classified-By: user1@phillip.liquidsys.com
    2  Classified-As: spam

       ------=_Part_23_13878940.114203
       Content-Type: message/rfc822
       Content-Disposition: attachment

    3  <Content of original message>

zmspamextract Utility
---------------------

The command line program `zmspamextract` can be used to dump the the
feedback mailboxes, and is a flexible tool for extracting messages
from the Zimbra store.  It uses SOAP to talk to Zimbra store servers.
It also reads LDAP for configuration information such as which store
server the target mailbox resides in.

It can extract messages from an account configured as the
`zimbraSpamIsSpamAccount (-s)`, or from `zimbraSpamIsNotSpamAccount (-n)`
or you can specify any account as the target (-m).

This tools needs to an admin auth token.  You can specify an admin
user and password with the -a and -p options.  On a mailbox server
admin auth information is available in local config and is read from
there.  On a non-mailbox server, the -a and -p options come in handy.
Also the admin service URL, to which the admin auth request must be
made, is defaulted to the admin service port on the target mailbox's
server.  If that mailbox server is not running admin service, then you
can use the -u option to specify the admin service URL.  Note that the
admin service URL has the form (trailing slash important):

    https://<host>:<port-usually-7071>/service/admin/soap/

You can also specify a query (-q) in order to extract the results of a
query, as opposed to extracting the default in:inbox.  You can ask
that messages be deleted on the server after extraction (-d).  Output
directory (-o) must be specified.  The raw (-r) option says to extract
the messages as-is without MIME parsing and retrieving the original
message from the feedback message.

Consult the --help option output for the complete list of options.

Different scenarios of zmspamextract use:

- You trust all your users to do a good job with Junk/Not Junk button
  usage.  You configure the global config variables, extract all
  messages and train your spam system.  This is the simple case that
  all the defaults are tuned for.

- You trust only some set of users with spam training.  Don't
  configure the global config options.  Instead tell your trusted
  users to forward their ham/spam messages as (`message/rfc822`)
  attachment to the ham/spam accounts.  Then use the -m option to
  extract from these ham/spam accounts.

- Note that whether you have the global config turned on or not, the
  spam/ham accounts can be sent `message/rfc822` attachments from
  Outlook, Thunderbird etc for spam training.

- You have a `MyHandSelectedSpamFolder` that contains spam messages.
  Then use the -m and -r (raw) options to dump this folder out for
  spam training.

Note that `zmspamextract` can also be used as a generic mailbox dumper,
but it get its name (instead of zmmsgextract) because of the -s and -n
options and the default being !raw.

Limitations
-----------

We don't have per user spam bayes database.  This appears to be a
limitation of amavisd-new.  The other issue is we must enhance
zmspamextract to process the spam report part and recognize which user
trained it.

Also have to worry about one user poisioning anothers spam database
(even if we block outsiders from emailing these accounts).  We need
mail policy for this and possibly multiple spam feedback gathering
accounts.

We also should be able to apply a per user and a global bayes database
against a single messgage (the case where a trusted few train the
global bayes database) - not sure if this is possible with SA.

There is no support for "oops, didn't mean to do that." If you hit
Junk button by mistake and you hit Not Junk again, we will train both
ways.  Is the fix to sort by date, and use the user's last decision?

SA has a --forget option where we can tell it to forget a particular
message.  There is no UI or Zimbrac command line support.  You can do
it manually by saving Show Original output and running sa-learn
--forget from command line.

Ideally we would make the spam/ham accounts reachable only through an
admin auth token.  Today, account status of 'locked' is not overriden
by an admin auth token - you still get auth credentials expired.  For
now, set a sufficiently long and random password on these accounts.
