package ch.stola3;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * Get all IssueEvents.<br>
     * <b>Order: newest top!</b>
     *
     * @return List<IssuePojo>
     */
    public List<IssuePojo> issueEvents() {
        return issueEvents;
    }

    /**
     * Get all IssueEvents.<br>
     * <b>Order: oldest entry top, newest bottom!</b>
     *
     * @return List<IssuePojo>
     */
    public List<IssuePojo> issueEventsReverse() {
        List<IssuePojo> reverse = new ArrayList<IssuePojo>(issueEvents);
        Collections.reverse(reverse);
        return reverse;
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
