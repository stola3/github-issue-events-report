# GitHub Issue Events Report

Goal: Export issue events to a report.

HINT: Works great in combination with: https://github.com/synox/BFH-Latex_Bericht_Template_Markdown

## Usage
`java -jar github-issue-events-report-0.1-jar-with-dependencies.jar`

### Installation
* Download the latest release (or build your own version with maven)
* Put your settings into `config.properties`
  * `repo.owner` = owner of the repo (user or organisation)
  * `repo.name`  = name of repo
  * `personal.access.token` = if you wish to export an private repository you need an access token
     * https://github.com/settings/tokens/new => scope [`repo`]
     * HINT: Unfortunately you have to grant access to `scope repo` to read private repo events! Keep in mind that scope `repo` grants read/write access, as a result of that should tread your private access token as a credential...

  * Shourtcut for the lazy ones
     * `echo -e "repo.owner=stola3\nrepo.name=github-issue-events-report\npersonal.access.token=" > config.properties`

Required folder structure:
```
./   # root-dir. run the jar from here.
   |-- config.properties    # repo configuration
   |-- github-issue-events-report-0.1-jar-with-dependencies.jar
   |-- report
     |.  # <== path were the generated reports will be placed
     |-- assets       # some provided asstes. eg. the github icons
     |-- templates    # <== put your templates into this folder!
```


# Example
**Template** (`report/templates/theExample.txt`)
```
GitHub Worklog
==============

Following {{countIssueEvent}} IssueEvents were exported at {{currentTime}} from GitHub Repo {{repoOwner}}/{{repoName}}.
* OnLine: https://github.com/{{repoOwner}}/{{repoName}}/issues

{{#issueEvents}}
#{{issueNr}} / {{dateTime}} / {{loginName}} / {{issueEventType.actionText}}
{{#milestone}}milestone: {{{milestone}}} / {{/milestone}}labels: {{#labels}} {{{name}}} {{/labels}}
title: {{{issueTitleEscaped}}}
{{#actionBodyEscaped}}body: {{{actionBodyEscaped}}}{{/actionBodyEscaped}}
----------------------------------------------------------------------------
{{/issueEvents}}

```

**Run the client**
```
java -jar github-issue-events-report-0.1-jar-with-dependencies.jar
```

**See the result** (`report/theExample.txt`)

```
GitHub Worklog
==============

Following 12 IssueEvents were exported at 22.03.2015 16:08 from GitHub Repo stola3/github-issue-events-report.
* OnLine: https://github.com/stola3/github-issue-events-report/issues

#1 / 21.03.2015 12:43 / stola3 / open issue
labels:  feature
title: Handle CLOSE issue events
body: So far Close issues are ignored.
----------------------------------------------------------------------------
#2 / 21.03.2015 12:47 / stola3 / open issue
labels:  enhancement
title: Add HOWTO use this project to README
body: At least some comments about the usage of this project would be helpful.
----------------------------------------------------------------------------
...
```

# Template
Thanks to mustache (http://mustache.github.io/) you can easily write your own templates.

List of all given attributes in Version 0.1:

## Variables

Variable  | Description
--------- | -------------
repoOwner |  repo: user or organisation |
repoName  |  repository name |
countIssueEvent  | total number of exported IssueEvent and IssueCommentEvent
currentTime  | Current time (format: `dd.MM.yyyy HH:mm`)

## Objects / List

### List of IssueEvents
* issueEvents - Newest entry is the first
* issueEventsReverse - Oldest entry is the first

But all Variables and Objects are equal.

issueEvent Variable  | Description
--------- | -------------
issueNr |  issue number
dateTime | date time of event
loginName | user who made the event
issueEventType.icon | GitHub icon (eg. issue-opened)
issueEventType.actionText | Description of the event (eg. issue commented)
milestone: | milestone
labels (list*) | List of labels (attributes: name and color)
 * attr: name |  label name
 * attr: name |  label color
issueTitle | issue title
issueTitleEscaped | issue title but escaped
actionBody | event body (issue content or comment)
actionBodyEscaped | event body actionBodyEscaped

### KeyOverview (List: issueEventKeyOverview)

* issueEventKeyOverview - List of all implemented IssueEvents.

This enables to print a key overview.

Variable |  Description
 ----  | ---
 icon | GitHub icon (eg. issue-opened)
 actionText | Description of the event (eg. issue commented)

Example (List )
```
{{#issueEvents}}
{{issueNr}} / {{dateTime}} / {{loginName}} / {{issueEventType.actionText}}
title: {{{issueTitle}}}
body: {{{actionBody}}}
{{/issueEvents}}
```

## Licence: MIT
* This project is published under `The MIT License (MIT)` (full licence in LICENCE)
* Feel free to reuse any part. Or even better fork my and publish your improvements!

## Development

Maven Dependencies:
* GitHub API Client: org.eclipse.egit.github.core
    * Why? GitHub API Client (I was to lazy to parse the JSON)
    * Licence: Eclipse Public License Version 1.0
    * Project: https://github.com/eclipse/egit-github/tree/master/org.eclipse.egit.github.core
* Mustache Template Engine: com.github.spullara.mustache.java
    * Why? Simplify creating new templates
    * Licence: Apache License, Version 2.0
    * Project: https://github.com/spullara/mustache.java
