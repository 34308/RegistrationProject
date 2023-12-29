package pl.edu.anstar.registration.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SaveDataWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveDataWorker.class);
    @JobWorker(type = "saveData")
    public void saveData(final JobClient client, final ActivatedJob job) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }LOGGER.info("save data");
    }
}
