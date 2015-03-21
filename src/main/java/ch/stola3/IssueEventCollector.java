package ch.stola3;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.EventPayload;
import org.eclipse.egit.github.core.event.IssueCommentPayload;
import org.eclipse.egit.github.core.event.IssuesPayload;
import org.eclipse.egit.github.core.service.EventService;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by stola3 on 21.03.15.
 */
public class IssueEventCollector {

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private GitHubClient gitHubClient;

    private RepositoryService service;
    private Repository repo;
    private EventService eventService;

    public IssueEventCollector(String repoOwner, String repoName) throws  IssueEventException{
        this(repoOwner, repoName, null);
    }

    public IssueEventCollector(String repoOwner, String repoName, String token) throws  IssueEventException{
        gitHubClient = new GitHubClient();
        if(token != null) {
            gitHubClient.setOAuth2Token(token);
        }

        try {
            service = new RepositoryService(gitHubClient);
            repo = service.getRepository(repoOwner, repoName);
            eventService = new EventService(gitHubClient);
        } catch (IOException e) {
            throw new IssueEventException("Can not initialize repo! Check you config!", e);
        }
    }

    public ArrayList<IssuePojo> collectIssueEvents() throws IOException {
        ArrayList<IssuePojo> issueEvents = new ArrayList<IssuePojo>();

        int nextPage = 0;
        while(nextPage >= 0) {
            PageIterator<Event> eventIter = eventService.pageEvents(repo, nextPage, 30);
            nextPage = eventIter.getNextPage();

            loopOverPage(issueEvents, eventIter);
        }


        return issueEvents;
    }

    private void loopOverPage(ArrayList<IssuePojo> issueEvents, PageIterator<Event> eventIter) {
        while (eventIter.hasNext()) {
            Collection<Event> events = eventIter.next();

            //events.stream().filter(t -> t.getType().equals("IssuesEvent")).map(Event::new);
            for (Event event : events) {

                IssuePojo.EventType eventType;
                try {
                    eventType = IssuePojo.EventType.valueOf(event.getType());
                } catch (IllegalArgumentException iae ) {
                    eventType = IssuePojo.EventType.UNHANDELD;
                    continue; // continue loop!

                }


                EventPayload eventPayload = event.getPayload();

                // fill generic information
                IssuePojo ie = new IssuePojo(eventType);
                ie.setLoginName(event.getActor().getLogin());
                ie.setDateTime(simpleDateFormat.format(event.getCreatedAt())); //TODO format?!?

                switch (eventType) {
                    case IssueCommentEvent:

                        handleIssueCommentEvent(ie, eventPayload);
                        break;
                    case IssuesEvent:

                        ie = handleIssueEvent(ie, eventPayload);
                        break;
                    default:
                        //should never happen, because of the continue above...
                        break;
                }
                issueEvents.add(ie);

            }
        }
    }


    private IssuePojo  handleIssueEvent(IssuePojo issuePojo, EventPayload eventPayload){
        IssuesPayload issuesPayload = (IssuesPayload) eventPayload;


        issuePojo.setIssueNr(issuesPayload.getIssue().getNumber());
        issuePojo.setIssueTitle(issuesPayload.getIssue().getTitle());

        if(issuesPayload.getIssue().getBody() != null ) {
            issuePojo.setActionBody(issuesPayload.getIssue().getBody());
        }

        // evaluate Issue Event Type with ugly switch case...
        switch (issuesPayload.getAction()) {
            case "opened":
                issuePojo.setIssueEventType(IssuePojo.IssueEventType.ISSUE_OPEN);
                break;

            case "closed":
                issuePojo.setIssueEventType(IssuePojo.IssueEventType.ISSUE_CLOSE);
                break;
            default:
                System.out.println(" !!! UNKNOWN : " + issuesPayload.getAction() + " Nr: " + issuesPayload.getIssue().getNumber());
                issuePojo.setIssueEventType(IssuePojo.IssueEventType.UNKNOWN);
                break;
        }


        return issuePojo;
    }


    //TODO use generics for .getIssue()
    private IssuePojo  handleIssueCommentEvent(IssuePojo issuePojo, EventPayload eventPayload){
        IssueCommentPayload issueCommentPayload  = (IssueCommentPayload) eventPayload;

        issuePojo.setIssueNr(issueCommentPayload.getIssue().getNumber());
        issuePojo.setIssueTitle(issueCommentPayload.getIssue().getTitle());

        if(issueCommentPayload.getComment().getBody() != null ) {
            issuePojo.setActionBody(issueCommentPayload.getComment().getBody());
        }

        switch (issueCommentPayload.getAction()) {
            case "created":
                issuePojo.setIssueEventType(IssuePojo.IssueEventType.ISSUE_COMMENTED);
                break;

            default:
                System.out.println(" !!! UNKNOWN : " + issueCommentPayload.getAction());
                issuePojo.setIssueEventType(IssuePojo.IssueEventType.UNKNOWN);
                break;
        }

        return issuePojo;
    }

}
