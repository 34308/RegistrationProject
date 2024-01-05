package pl.edu.anstar.registration.worker;

import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SaasAuthentication;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.anstar.registration.Services.ActivationService;
import pl.edu.anstar.registration.exception.ActivationException;
import pl.edu.anstar.registration.model.User;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ActivateAccountWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivateAccountWorker.class);
    private final ActivationService activationService;

    @Value("${zeebe.client.cloud.clusterId:N/A}")
    private String clusterId;

    @Value("${zeebe.client.cloud.region:N/A}")
    private String region;
    @Value("${zeebe.client.cloud.clientId:N/A}")
    private String clientId;

    @Value("${zeebe.client.cloud.clientSecret:N/A}")
    private String clientSecret;
    @JobWorker(type = "activateAccount")
    public void activateAccount(final JobClient client, final ActivatedJob job) throws TaskListException {
        LOGGER.info("activate account");
        Map<String, Object> vars = job.getVariablesAsMap();
        String activationKey = (String)vars.get("activationKey");
        try {
            activationService.activateAccount(activationKey);
            LOGGER.info("account activated");
        } catch (ActivationException exception) {
            handleError(client, job, "ACTIVATION_FAILED", exception.getMessage());
        }

    }

    private void handleError(JobClient client, ActivatedJob job, String errorCode, String errorMessage) {
        LOGGER.error(errorCode + "," + errorMessage);

        client.newThrowErrorCommand(job.getKey())
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .send()
                .join();
    }
}
