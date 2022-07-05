# Developing Carbonio Mailbox

___

Table of Content:

- [Development Setup](#development-setup)
- [Running Tests](#running-tests)
- [Coding Conventions](#coding-conventions)
- [Commit Message Guidelines](#commit-message-guidelines)
- [Documentation and Code generation](#documentation-and-code-generation)

___

## Development Setup

Carbonio Mailbox is a Java project that uses ant build system allowing to
compile, assemble, test the code.

### Project structure

Carbonio Mailbox source code contains several components, which are organised
as java modules, notably:

- **client**: package to interact with the mailbox
- **common**: package providing classes of common use, like utilities, clients
and common parameters
- **native**: package to load native libraries
- **soap**: package describing SOAP APIs
- **store**: package with core implementations and integrations like SOAP APIs,
LDAP, Krb5, IMAP, POP3 and CLI functions

### Building from source

- Build Requirements:
  - JDK version 11, to confirm run `javac -version`
  - ant
  - apache-ant-contrib

1. Clone the carbonio-mailbox repository:

    `git clone https://github.com/Zextras/carbonio-mailbox.git`

2. Enter into source directory:

    `cd carbonio-mailbox/`

3. Build the code:

    `ant all -Dcarbonio.buildinfo.version=22.6.1_ZEXTRAS_202206`

___

## Running Tests

1. Enter into source directory:

    `cd carbonio-mailbox/`

2. Run JUnit tests:

    `ant test-all-plough-through -Dcarbonio.buildinfo.version=22.6.1_ZEXTRAS_202206`

___

## Coding conventions

Code conventions improve software readability, allowing engineers to grasp new/old
code more quickly and thoroughly.

### Naming convention

We simply practise following Oracle's official naming convention for Java that
is documented [here on their website](https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html)

### Git hook

For this project, we use [pre-commit](https://pre-commit.com/) to manage
git hooks; all you need to do is [install](https://pre-commit.com/#install)
pre-commit and configure the pre-commit hook as instructed in the tool's
usage guide. This will format the source files in the required format whenever
you will commit a change.

___

## Commit Message Guidelines

We have strict guidelines for how our git commit messages should be formatted.
This results in more understandable messages that are easier to follow when
browsing the project history. Furthermore, these git commit messages are also
used to build Carbonio Mailbox change log.

### Pre commit hook to enforce semantic-commit

We enforce semantic commit messages using git hook called
[semantic-commit-hook](https://github.com/soerenschneider/conventional-pre-commit-hook).
To use this you just need to configure pre-commit on local clone of the repository.

### Commit Message Format

Each commit message consists of a **header**, a **body** and a **footer**.
The header has a special format that includes a **type**, a **scope** and a **subject**:

```bash
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

The **header** is mandatory while the **scope** of the header is optional.

Any line of the commit message must be no more than 100 characters long!
This makes the message simpler to read on GitHub and in other git tools.

### Revert

If the commit reverts a previous commit, it should begin with `revert:`,
followed by the header of the reverted commit.
In the body it should say: `This reverts commit <hash>.`, where the hash
is the SHA of the commit being reverted.

### Type

Must be one of the following:

- **feat**: A new feature
- **fix**: A bug fix
- **docs**: Documentation only changes
- **style**: Changes that do not affect the meaning of the code (white-space,
  formatting, missing semi-colons, etc)
- **refactor**: A code change that neither fixes a bug nor adds a feature
- **perf**: A code change that improves performance
- **test**: Adding missing or correcting existing tests
- **chore**: Changes to the build process or auxiliary tools and libraries such
  as documentation generation

### Scope

The scope could be anything specifying place of the commit change.
For example ldap, smtp, soap etc

You can use * when the change affects more than a single scope.

### Subject

The subject contains succinct description of the change:

- use the imperative, present tense: "change" not "changed" nor "changes"
- don't capitalize first letter
- no dot (.) at the end

### Body

Just as in the subject, use the imperative, present tense: "change" not
"changed" nor "changes". The body should include the motivation for the
change and contrast this with previous behavior.

### Footer

The footer should contain any information about Breaking Changes and is
also the place to [reference GitHub issues that this commit closes](https://docs.github.com/en/issues/tracking-your-work-with-issues/linking-a-pull-request-to-an-issue)

Breaking Changes should start with the word BREAKING CHANGE: with a space
or two newlines. The rest of the commit message is then used for this.

### Example

```bash
feat(soap-servlet): onUrlChange event

Added new event to soap-servlet:
- forward popstate event if available
- forward hashchange event if popstate not available
- do polling when neither popstate nor hashchange available

Closes #392
Breaks foo.bar api, foo.baz should be used instead
```

___

## Documentation and code generation

### Documentation

Since most of the code is in Java we use/follow javadoc tool to generate
documentation wherever required and follow the code documentation guidelines
provided by [Oracle Javadoc reference page](https://www.oracle.com/in/technical-resources/articles/java/javadoc-tool.html)

### Code Generation

To maintain a few Classes in mailbox, we use a few code generation utilities
that come in useful and relieve us of the effort of creating boilerplate code
that can be generated automatically using document-centric files such as XML.

These utilities are included in their respective modules and can be accessed
as an ant target of that specific module.

In the `store` module, for example, we have `generate-getters` target which is
used to generate getter, setter, and unsetters methods using the
`store/conf/attrs.xml` file which is used to define LDAP attributes in the
system. Running this target will add entries to corresponding source classes.
