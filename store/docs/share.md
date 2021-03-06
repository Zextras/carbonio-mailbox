Users can share folders of various types (mail, calendar, etc) by
granting access to specific users, groups, or domains. Folders can
also be shared with external users and can be accessed via the REST
interface. The FolderActionRequest is used to grant such access
and is documented in soap.txt.

When a folder is shared, the client sends, at the user's discretion,
an email to the grantee notifying them of the new share or a change
to an existing share. This email contains a MIME part with the
following hierarchy:

    multipart/alternative
        text/plain
        text/html
        xml/x-share

The text/plain and text/html parts are human-readable messages
describing the share, whereas the xml/x-share part is an
XML document meant for processing by the server/client.

The XML part of the message conforms to the following DTD grammar:

    <!ENTITY % actions "new|edit|delete|accept|decline">

    <!ELEMENT share (grantee,grantor,link,notes)>
    <!ATTLIST share xmlns   CDATA       #FIXED "urn:zimbraShare"
                    version CDATA       #FIXED "0.1"
                    action  (%actions;) #REQUIRED>

    <!ELEMENT grantee EMPTY>
    <!ATTLIST grantee id    CDATA #REQUIRED
                      email CDATA #REQUIRED
                      name  CDATA #REQUIRED>

    <!ELEMENT grantor EMPTY>
    <!ATTLIST grantor id    CDATA #REQUIRED
                      email CDATA #REQUIRED
                      name  CDATA #REQUIRED>

    <!ELEMENT link EMPTY>
    <!ATTLIST link id    CDATA   #REQUIRED
                   name  CDATA   #REQUIRED
                   view  NMTOKEN #IMPLIED
                   perm  CDATA   #REQUIRED>

    <!ELEMENT notes (#PCDATA)>

So an xml/x-share part will take the following form:

    <share xmlns="urn:zimbraShare" version="0.1" action="{action}" >
      <grantee id="{zid}" email="{email}" name="{display name}" />
      <grantor id="{zid}" email="{email}" name="{display name}" />
      <link id="{folderId}" name="{folderName}" view="{view}" perm="{perms}" />
      <notes> {...notes...} </notes>
    </share>

Notes:

````
  {action}   = The share action. Allowed values: "new", "edit",
               "delete", "accept", or "decline".
  {zid}      = Zimbra id of the account. If the folder is shared with
               an external user, then the grantee id attribute is
               meaningless. [In this case, the client currently sets
               this value to the external user's email address.]
  {folderId} = The local id of the folder in the grantor's mailbox.
  {view}     = The default folder view. Defined in soap.txt.
  {perms}    = The permissions being granted. Defined in soap.txt.
````

Here is an example of a new share notification document for a
shared calendar:

    Date: Fri, 30 Mar 2007 13:12:22 -0700 (PDT)
    From: Demo User One <user1@localhost>
    To: user2@localhost
    Message-ID: <9296236.01175285542280.JavaMail.noname@localhost>
    Subject: Share Created
    MIME-Version: 1.0
    Content-Type: multipart/alternative;
      boundary="----=_Part_0_4397075.1175285542201"
    X-Originating-IP: [127.0.0.1]

    ------=_Part_0_4397075.1175285542201
    Content-Type: text/plain; charset=utf-8
    Content-Transfer-Encoding: 7bit

    The following share has been created:

    Shared item: Calendar (Calendar Folder)
    Owner: Demo User One

    Grantee: user2
    Role: Viewer
    Allowed actions: View

    ------=_Part_0_4397075.1175285542201
    Content-Type: text/html; charset=utf-8
    Content-Transfer-Encoding: 7bit

    <h3>The following share has been created:</h3>
    <p>
    <table border="0">
    <tr><th align="left">Shared item:</th><td>Calendar (Calendar Folder)</td></tr>
    <tr><th align="left">Owner:</th><td>Demo User One</td></tr>
    </table>
    </p>
    <table border="0">
    <tr><th align="left">Grantee:</th><td>user2</td></tr>
    <tr><th align="left">Role:</th><td>Viewer</td></tr>
    <tr><th align="left">Allowed actions:</th><td>View</td>
    </tr>
    </table>
    ------=_Part_0_4397075.1175285542201
    Content-Type: xml/x-share; charset=utf-8
    Content-Transfer-Encoding: 7bit

    <share xmlns="urn:zimbraShare" version="0.1" action="new" >
      <grantee id="f2a00a30-af10-4071-85eb-0965de751c1c" email="user2@localhost" name="user2" />
      <grantor id="7d7af28c-cb79-44d3-b09f-7d4d6ad63774" email="user1@localhost" name="Demo User One" />
      <link id="10" name="Calendar" view="appointment" perm="r" />
      <notes></notes>
    </share>
    ------=_Part_0_4397075.1175285542201--
