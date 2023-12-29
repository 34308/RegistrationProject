package pl.edu.anstar.registration.Services;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.CompleteJobResponse;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobWorker;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

import static io.camunda.zeebe.client.impl.Loggers.LOGGER;


@Service
public class UserTaskHandlerService {

    @ZeebeWorker(type = "io.camunda.zeebe:userTask")
    public void handleJob(final JobClient client, final ActivatedJob job) {
        try {
            Thread.sleep(10000); // Sleep for 10 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println(job.getElementId());
        Map variables = job.getVariablesAsMap();

        client.newCompleteCommand(job.getKey())
                               .send()
                .join();
    }
}
