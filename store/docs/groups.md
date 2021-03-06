REQUIREMENTS
============
When sharing calendars, folders, etc, it becomes a burden to grant access by 
accounts only. You should be able to grant access to a whole "group" of users. 

Instead of adding a new named entity to the system (i.e., "groups"), we decided
to leverage the existing distribution list (DL) infrastructure and enable DLs
to be used as groups. We use the term group and DL interchangeably from this point.

This has a number of benefits:

  - get to re-use the existing DL infrastructure, including all the admin UI
    support.
     
  - users (admin and end-users) don't have to learn two different concepts,
    they just know and understand a single concept, which is a named group
    of users that they can send mail to and/or use in ACLs.
    
  - admins don't have to maintain two parallel lists of users. i.e., they 
    don't need to maintain the "engineering group" and the "engineering DL".
  
  - group discovery can be done via the GAL
  
  - groups can contain other groups

IMPLEMENTATION
==============

## What makes a distribution list a group?
All distribution lists are groups.

## GROUPS OF GROUPS
DLs can contain other DLs, and thus groups can contain other groups. If a user
is in DL A, and DL A is also in DL B, then the user will be in both groups. For
example, lets consider the case where all@test.com and engineering@test.com are both
DLs that are groups, and engineering is a member of "all".

```
prov> cd test.com 
d76cc24c-9bd6-4ef2-90c2-88bebabb208b
prov> cdl all@test.com
711ccbd1-86c9-494a-b561-dca40a864b1b
prov> cdl engineering@test.com
ae18f02a-efd0-4066-b8f3-f14b8629a6b6
prov> adlm all@test.com engineering@test.com
prov> adlm engineering@test.com user1@test.com
```

If we look at `all@test.com`, we see that it has one member, `engineering@test.com`:

```
prov> gdlm all@test.com
# distributionList all@test.com memberCount=1
mail: all@test.com
objectClass: zimbraDistributionList
objectClass: zimbraMailRecipient
uid: all
zimbraId: 711ccbd1-86c9-494a-b561-dca40a864b1b
zimbraMailAlias: all@test.com
zimbraMailForwardingAddress: engineering@test.com
zimbraMailStatus: enabled
```

If we look at `engineering@etst.com`, we see that is has one member, `user1@test.com`:

```
prov> gdl engineering@test.com
# distributionList engineering@test.com memberCount=1
mail: engineering@test.com
objectClass: zimbraDistributionList
objectClass: zimbraMailRecipient
uid: engineering
zimbraId: ae18f02a-efd0-4066-b8f3-f14b8629a6b6
zimbraMailAlias: engineering@test.com
zimbraMailForwardingAddress: user1@test.com
zimbraMailStatus: enabled
```

If we look at all the DLs that user1 belongs to, we get both `all@test.com` and
`engineering@test.com`:

```
prov> gam user1@test.com
all@test.com (via engineering@test.com)
engineering@test.com
```

## CALCULATING A USER'S GROUP MEMBERSHIP LIST
When calculating all the groups a user belongs to, we don't just stop at all the
Dls that a user belongs to. If we did, then user1 wouldn't be a member of the all@test.com group,
even though logically they should be.

In order to calculate all groups a user belongs to, we need to not only include all
groups the user directly belongs, we also need to include all groups listed
that those group's belong to, and so on.

This algorithm is actually fairly trivial to implement, and is described (implemented 
slightly differently) here:

<http://middleware.internet2.edu/dir/groups/rpr-nmi-edit-mace_dir-groups_best_practices-1.0.html>

The algorithm is:

````
Step 1: Initialize the set "groups" to the empty set. When we are done, this set will
        contain all groups the user belongs to.

Step 2: Add all groups listed the user is directly a member of to the "groupsToCheck" set.
        
Step 3: while group in  "groupsToCheck":
            if group not in groups // stop cycles
                 add group to "groups", remove from groupsToCheck
                 for all groups that group is a member of add to "groupsToCheck" 
            end
        end
````

    groups: {}
    groupsToCheck: {ae18f02a-efd0-4066-b8f3-f14b8629a6b6}

After checking the group, we'd see that it is a member of a group, and we'd then have:

    groups: {ae18f02a-efd0-4066-b8f3-f14b8629a6b6}
    groupsToCheck: {711ccbd1-86c9-494a-b561-dca40a864b1b}

We'd then see that the `all@test.com` group isn't a member of any other groups and end
up with:

    groups: {ae18f02a-efd0-4066-b8f3-f14b8629a6b6, 711ccbd1-86c9-494a-b561-dca40a864b1b}     
    groupsToCheck: {}

And we'd stop with user1 belonging to two groups.
   
ZMPROV CHANGES
==============

and a new command to get all the DLs a user belongs to (including any DLs they belong to
due to DLs being included in DLs):

    GetAccountMembership(gam) {name@domain|id}
    
    
NOTES
=======================

## Remote Users
Only local users (a user that Zimbra is authoritative for) get access rights when
added to a DL. For example, adding `joe.random@yahoo.com` a DL that is used in an ACL 
doesn't grant them access.

In the future, it will be possible for two Zimbra installations to set up a trust
relationship, in which case adding a remote user from one of those domains could actually
grant access to local resources.

One could also imagine a scenario where adding a remote user to a DL that is a group mails
said user some information that the user can use to remotely access protected data.


## issues:

- privacy. need to be able to make DL membership private, this will probably require
           postfix to auth. This is for HIPPA/FERPA compliance.


- user-created groups/DLs would be nice a feature. Maybe a requirement that they be prefixed with
 the user's primary email address followed by a "-" or some other prefix/suffix. 

- ACLs on DLs to control who can manage them

- well known DLs - could use WKD for fine-grained access control (i.e., account-read DL
  membership could allow its members read access to accounts in the admin console).
