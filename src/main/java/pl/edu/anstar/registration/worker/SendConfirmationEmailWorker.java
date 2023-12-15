package pl.edu.anstar.registration.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SendConfirmationEmailWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendConfirmationEmailWorker.class);
    @JobWorker(type = "sendConfirmationEmail")
    public void sendConfirmationEmail(final JobClient client, final ActivatedJob job) {
        LOGGER.info("send confirmation email");
    }
}
