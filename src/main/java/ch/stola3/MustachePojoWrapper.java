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

    public MustachePojoWrapper(List<IssuePojo> issueEvents) {
        this.issueEvents = issueEvents;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        currentTime = LocalDateTime.now().format(formatter).toString();
    }

    public List<IssuePojo> issueEvents() {
        return issueEvents;
    }


}
