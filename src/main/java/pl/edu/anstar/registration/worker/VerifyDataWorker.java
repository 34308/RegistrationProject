package pl.edu.anstar.registration.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VerifyDataWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyDataWorker.class);

    @JobWorker(type = "verifyData")
    public void verifyData(final JobClient client, final ActivatedJob job) {
        LOGGER.info("verify data");
    }
}
