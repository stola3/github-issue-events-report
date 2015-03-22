# GitHub Worklog Report {.unnumbered}

Folgende {{countIssueEvent}} IssueEvents wurden am {{currentTime}} vom Repo {{repoOwner}}/{{repoName}} exportiert.


**Legende:**

{{#issueEventKeyOverview}}
\includegraphics[scale=0.15]{bilder/github/png/{{icon}}.png} {{actionText}} \newline
{{/issueEventKeyOverview}}

* OnLine: https://github.com/{{repoOwner}}/{{repoName}}/issues

\begin{center}
\line(1,0){450}
\end{center}

{{#issueEventsReverse}}

\begin{small}
\includegraphics[scale=0.25]{bilder/github/png/{{issueEventType.icon}}.png} {{dateTime}} / {{loginName}}

{\large \#{{issueNr}} : }{{{issueTitleEscaped}}}

{{{actionBodyEscaped}}}
\end{small}

\begin{center}
\line(1,0){450}
\end{center}

{{/issueEventsReverse}}
