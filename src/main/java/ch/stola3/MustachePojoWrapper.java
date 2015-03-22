package ch.stola3;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * alias NonsenseWrapper just wraps the IssuePojo for Mustache...
 */
public class MustachePojoWrapper {

    private List<IssuePojo> issueEvents;

    public String currentTime;

    public String repoOwner;

    public String repoName;

    public MustachePojoWrapper(List<IssuePojo> issueEvents) {
        this.issueEvents = issueEvents;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        currentTime = LocalDateTime.now().format(formatter).toString();
    }

    public List<IssuePojo> issueEvents() {
        return issueEvents;
    }

    public int countIssueEvent() {
        if (issueEvents == null) {
            return 0;
        } else {
            return issueEvents.size();
        }
    }

    /**
     * @return
     */
    public IssuePojo.IssueEventType[] getIssueEventLegend() {

        return IssuePojo.IssueEventType.values();
    }

}
