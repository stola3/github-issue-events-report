package ch.stola3;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by stola3 on 15.03.15.
 */
public class GitHubEventsReport {

    private static final String PATH_2_TEMPLATES = "report/templates";

    private String repoOwner = "stola3"; // default
    private String repoName = "ourwish.ch"; // default

    private String accessToken;

    private List<String> templates;

    public GitHubEventsReport() {
        templates = new ArrayList<>();

        // load templates, if available otherwise use default
        templates = this.getAllTemplates();
        if (templates == null || templates.size() == 0) {
            System.out.println("No templates found under: " + PATH_2_TEMPLATES + " / However, use default");
            templates.add("issueReport.txt");
        }
    }

    public static void main(String[] args) throws IOException, IssueEventException {
        GitHubEventsReport gher = new GitHubEventsReport();

        // init
        try {
            gher.initConfig();
        } catch (IOException e) {
            System.out.println("CAN NOT FIND or READ config.properties ! Continue with default...");
            e.printStackTrace();
            //return;
        }

        System.out.println("Init done. Start collecting IssueEvents...");

        // collect
        ArrayList<IssuePojo> issueEvents = gher.doCollect();
        System.out.println("Got " + issueEvents.size() + " IssueEvents. Next step exporting with mustache templates (path='" + PATH_2_TEMPLATES + "').");

        // export
        gher.doExport(new MustachePojoWrapper(issueEvents));
        System.out.println("All done... ByeBye!");
    }

    private List<String> getAllTemplates() {
        List<String> templates = new ArrayList<>();
        try {
            Files.walk(Paths.get(PATH_2_TEMPLATES)).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    templates.add(filePath.toAbsolutePath().toString());
                }
            });
        } catch (IOException e) {
            // ignore
        }
        return templates;
    }

    private void initConfig() throws IOException {
        Properties mainProperties = new Properties();

        FileInputStream file;

        String path = "config.properties";
        file = new FileInputStream(path);
        mainProperties.load(file);
        file.close();

        // retrieve the property we are intrested
        if (mainProperties.getProperty("repo.owner") != null && mainProperties.getProperty("repo.name") != null) {
            repoOwner = mainProperties.getProperty("repo.owner");
            repoName = mainProperties.getProperty("repo.name");
        }

        String tokenKey = "personal.access.token";
        if (mainProperties.getProperty(tokenKey) != null && mainProperties.getProperty(tokenKey).length() > 0) {
            accessToken = mainProperties.getProperty(tokenKey);
        }

    }

    private ArrayList<IssuePojo> doCollect() throws IssueEventException, IOException {
        IssueEventCollector iec = new IssueEventCollector(repoOwner, repoName, accessToken);
        return iec.collectIssueEvents();
    }

    private void doExport(MustachePojoWrapper mustachePojoWrapper) {
        MustacheFactory mf = new DefaultMustacheFactory();

        // enrich repo information
        mustachePojoWrapper.repoOwner = repoOwner;
        mustachePojoWrapper.repoName = repoName;

        try {


            for (String curTemplate : templates) {
                //TODO remove this hack!
                String reportFileName = curTemplate.replaceAll("/templates", "");

                System.out.println(">>>>>> write template " + curTemplate + " >> to >> " + reportFileName);

                Mustache curMustache = mf.compile(curTemplate);

                curMustache.execute(new PrintWriter(new File(reportFileName)), mustachePojoWrapper).flush();
                //curMustache.execute(new PrintWriter(System.out), new NonseneWrapper(issueEvents)).flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
