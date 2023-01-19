package pl.edu.anstar.recruitment;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import pl.edu.anstar.recruitment.mail.anstar.MailTemplate;
import pl.edu.anstar.recruitment.model.Faculty;
import pl.edu.anstar.recruitment.model.CandidateApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class CandidateApplicationAdapter {

    private static Logger LOG = LoggerFactory.getLogger(CandidateApplicationAdapter.class);

    @Value("${radon.nauka.gov.pl.apiKey:N/A}")
    private String API_KEY;
    @Value("${radon.nauka.gov.pl.url:N/A}")
    private String URL;

    @Value("${spring.mail.host:N/A}")
    private String host;
    @Value("${spring.mail.port:N/A}")
    private String port;
    @Value("${spring.mail.username:N/A}")
    private String username;
    @Value("${spring.mail.password:N/A}")
    private String password;

    @Value("${database.postgresql.dburl:N/A}")
    private String dbUrl;

    @Value("${database.postgresql.dbuser:N/A}")
    private String dbUser;

    @Value("${database.postgresql.dbpassword:N/A}")
    private String dbPassword;

    @Value("${mail.anstar.edu.pl.mailsmtphost:N/A}")
    private String mailSmtpHost;

    @Value("${mail.anstar.edu.pl.mailuser:N/A}")
    private String mailUser;

    @Value("${mail.anstar.edu.pl.mailpassword:N/A}")
    private String mailPassword;

    @Value("${mail.anstar.edu.pl.mailsendermail:N/A}")
    private String mailSenderMail;

    @Value("${mail.anstar.edu.pl.mailsendername:N/A}")
    private String mailSenderName;

    @JobWorker(type = "registerApplication")
    public Map<String, Object> registerApplication(final JobClient client, final ActivatedJob job) {
        HashMap<String, Object> jobResultVariables = new HashMap<>();

        LOG.info("Job registerApplication is started.");

        final Map<String, Object> jobVariables = job.getVariablesAsMap();
        for (Map.Entry<String, Object> entry : jobVariables.entrySet()) {
            LOG.info("Job variable (process variable & inputted variable): " + entry.getKey() + " : " + entry.getValue());
        }

        int points = 0;
        try {
            points = Integer.parseInt((String) job.getVariablesAsMap().get("points"));
            LOG.info("Points. {}", points, "points.");
        } catch (NumberFormatException e) {
            LOG.info("Cannot convert String to int. {}", e);
        }

        CandidateApplication candidateApplication = new CandidateApplication(
                (String) job.getVariablesAsMap().get("firstName"),
                (String) job.getVariablesAsMap().get("lastName"),
                (String) job.getVariablesAsMap().get("email"),
                points,
                (String) job.getVariablesAsMap().get("faculty"),
                (boolean) job.getVariablesAsMap().get("olympic")
                //(String) job.getVariablesAsMap().get("decision")
        );

        int applicationId = Database.addApplication(candidateApplication, dbUrl, dbUser, dbPassword);
        if (applicationId > 0) {
            candidateApplication.setApplicationId(applicationId);
            LOG.info("Application registered. Application ID: {}", applicationId);
            jobResultVariables.put("applicationRegistered", true);
        } else {
            LOG.info("Application not registered.");
            jobResultVariables.put("applicationRegistered", false);
        }
        jobResultVariables.put("candidateApplication", candidateApplication);
        jobResultVariables.put("applicationId", applicationId);

        return jobResultVariables;
    }


    @JobWorker(type = "sendEmailWithANS")
    public Map<String, Object> sendEmailWithANS(final JobClient client, final ActivatedJob job) {
        HashMap<String, Object> jobResultVariables = new HashMap<>();

        LOG.info("Job sendEmail is started.");
        final Map<String, Object> jobVariables = job.getVariablesAsMap();
        for (Map.Entry<String, Object> entry : jobVariables.entrySet()) {
            LOG.info("Job variables (process & task input): {}", entry.getKey() + " : " + entry.getValue());
        }

        MailTemplate mt = new MailTemplate(0, (String) job.getVariablesAsMap().get("firstName") + " " + (String) job.getVariablesAsMap().get("lastName"), (String) job.getVariablesAsMap().get("email"), null, (Integer) job.getVariablesAsMap().get("applicationId"));
        try {
            mt.sendMail(mailSmtpHost, mailUser, mailPassword, mailSenderMail, mailSenderName);
            LOG.info("Sending mail succeeded.");
            jobResultVariables.put("mailSendingResult", true);
        } catch (Exception e) {
            LOG.error("Sending mail failed.");
            jobResultVariables.put("mailSendingResult", false);
        }

        return jobResultVariables;
    }

    @JobWorker(type = "registerDecision")
    public Map<String, Object> registerDecision(final JobClient client, final ActivatedJob job) {
        HashMap<String, Object> jobResultVariables = new HashMap<>();

        LOG.info("Job registerDecision is started.");
        final Map<String, Object> jobVariables = job.getVariablesAsMap();
        for (Map.Entry<String, Object> entry : jobVariables.entrySet()) {
            LOG.info("Job variable (process variable & inputted variable): " + entry.getKey() + " : " + entry.getValue());
        }

        String decision = (String) job.getVariablesAsMap().get("decision");
        int applicationId = (Integer) job.getVariablesAsMap().get("applicationId");

        int countUpdatedRows = Database.updateApplicationDecision(applicationId, decision, dbUrl, dbUser, dbPassword);
        if (countUpdatedRows > 0) {
            LOG.info("Decision registered. Application ID: {}", applicationId + " / " + "Decision: " + decision);
            jobResultVariables.put("decisionRegistered", true);
        } else {
            LOG.info("Decision not registered.");
            jobResultVariables.put("decisionRegistered", false);
        }

        return jobResultVariables;
    }

    @JobWorker(type = "verifyInRadon")
    public void verifyInRadon(final JobClient client, final ActivatedJob job) {

        LOG.info("Job verifyInRadon is started.");

        String url =  URL + "&courseName=" + (String) job.getVariablesAsMap().get("faculty");
        LOG.info("URL: {}", url);

        HttpClient radonClient = new HttpClient(url, API_KEY);
        Faculty faculty = radonClient.facultyRequest();

        LOG.info("RADON : Faculty name: {}", faculty.getFacultyName());
        LOG.info("RADON : Faculty profile: {}", faculty.getFacultyProfile());
        LOG.info("RADON : Faculty level: {}", faculty.getFacultyLevel());
        LOG.info("RADON : Faculty title: {}", faculty.getFacultyTitle());
        LOG.info("RADON : Faculty form: {}", faculty.getFacultyForm());
        LOG.info("RADON : Faculty number of semesters: {}", faculty.getFacultyNumberOfSemesters());

        if(faculty.getFacultyName().equalsIgnoreCase("N/D")) {
            client.newThrowErrorCommand(job.getKey())
                    .errorCode("FACULTY_UNAVAILABLE")
                    .send()
                    .join();
            LOG.info("Error FACULTY_UNAVAILABLE thrown.");
        } else {
            // Another way of ending job.
            client.newCompleteCommand(job.getKey())
                    .variables("{\"radonFacultyName\": " + "\"" + faculty.getFacultyName() + "\"" + ", \"radonFacultyProfile\":" + "\"" + faculty.getFacultyProfile() + "\"" + ", \"radonFacultyLevel\":" + "\"" + faculty.getFacultyLevel() + "\"" + ", \"radonFacultyForm\":" + "\"" + faculty.getFacultyForm() + "\"" + ", \"radonFacultyTitle\":" + "\"" + faculty.getFacultyTitle() + "\"" + ", \"radonFacultyNumberOfSemesters\":" + "\"" + faculty.getFacultyNumberOfSemesters() + "\"" + "}")
                    .send()
                    .exceptionally(throwable -> {
                        throw new RuntimeException("Could not complete job " + job, throwable);
                    });
        }
        LOG.info("Job verifyInRadon is ended.");
    }

    @JobWorker(type = "sendEmail")
    public Map<String, Object> sendGmail(final JobClient client, final ActivatedJob job) {
        HashMap<String, Object> jobResultVariables = new HashMap<>();

        LOG.info("Job sendGmail is started.");
        final Map<String, Object> jobVariables = job.getVariablesAsMap();
        for (Map.Entry<String, Object> entry : jobVariables.entrySet()) {
            LOG.info("Job variables (process & task input): {}", entry.getKey() + " : " + entry.getValue());
        }

        try {
            String from = "tpotempa@gmail.com";
            String fromName = "Akademia Tarnowska";
            String toAddress = (String) job.getVariablesAsMap().get("email");
            String toAddressName = job.getVariablesAsMap().get("firstName") + " " + (String) job.getVariablesAsMap().get("lastName");
            String ccAddresses = "";
            String bccAddresses = "";
            String subject = "Informacja o rejestracji aplikacji";
            String body = "Aplikacja na kierunek " + (String) job.getVariablesAsMap().get("radonFacultyName") +
                          ", studia " + (String) job.getVariablesAsMap().get("radonFacultyLevel") +
                          ", studia " + (String) job.getVariablesAsMap().get("radonFacultyForm") +
                          ", profil " + (String) job.getVariablesAsMap().get("radonFacultyProfile") +
                          " o czasie trwania " + (String) job.getVariablesAsMap().get("radonFacultyNumberOfSemesters") +
                          " semestrów" +
                          " z tytułem zawodowym " + (String) job.getVariablesAsMap().get("radonFacultyTitle") +
                          " została zarejestrowana. " +
                          "Identyfikator aplikacji: " + (Integer) job.getVariablesAsMap().get("applicationId");

            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(host);
            mailSender.setPort(Integer.parseInt(port));

            mailSender.setUsername(username);
            mailSender.setPassword(password);

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");

            GmailService gs = new GmailService(mailSender);
            gs.sendMail(from, fromName, subject, toAddress, ccAddresses, bccAddresses, body);
        } catch (Exception e) {
            LOG.error("Task exception.", e);
        }
        return jobResultVariables;
    }

}