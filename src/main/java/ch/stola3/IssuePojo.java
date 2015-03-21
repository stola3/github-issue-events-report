package ch.stola3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stola3 on 20.03.15.
 */
public class IssuePojo {

    /**
     * Escape helper to enable markdown and latex exports... There are much better ways to do that. However, just do it.
     *
     * @param escapeMe
     * @return escaped String
     */
    private String doEscape(String escapeMe) {
        if(escapeMe == null) {
            return null;
        }
        String escaped = escapeMe;

        escaped = escaped.replace("#", "\\#");
        escaped = escaped.replace("_", "\\_");

        return escaped;
    }


    public enum EventType {
        IssuesEvent, IssueCommentEvent, UNHANDELD;
    }

    public enum IssueEventType {
        ISSUE_OPEN("open issue", "issue-opened"),
        ISSUE_CLOSE("closed issue" , "issue-closed"),
        ISSUE_COMMENTED("issue commented", "comment"),
        UNKNOWN("not jet implemented" , "question")
        ;

        private String actionText;

        private String icon;

        private IssueEventType(String actionText, String icon) {
            this.actionText = actionText;
            this.icon = icon;
        }

        public String getActionText() {
            return actionText;
        }

        public String getIcon() {
            return icon;
        }
    }

    public IssuePojo(EventType eventType) {
        this.eventType = eventType;
    }

    public static class Label{
        private String name;
        private String color;

        public Label(String name, String color){
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    private String loginName;

    private String dateTime;

    private EventType eventType;

    private IssueEventType issueEventType;

    private int issueNr;

    private String issueTitle;

    private String actionBody;

    private List<Label> labels = new ArrayList<>();

    private String milestone;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getIssueNr() {
        return issueNr;
    }

    public void setIssueNr(int issueNr) {
        this.issueNr = issueNr;
    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public String getIssueTitleEscaped() {
        return doEscape(issueTitle);
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public IssueEventType getIssueEventType() {
        return issueEventType;
    }

    public void setIssueEventType(IssueEventType issueEventType) {
        this.issueEventType = issueEventType;
    }

    public String getActionBody() {
        return actionBody;
    }

    public String getActionBodyEscaped() {
        return doEscape(actionBody);
    }

    public void setActionBody(String actionBody) {
        this.actionBody = actionBody;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

}
