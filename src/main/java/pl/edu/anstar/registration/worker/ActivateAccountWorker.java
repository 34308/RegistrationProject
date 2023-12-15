package pl.edu.anstar.registration.worker;

import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SaasAuthentication;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ActivateAccountWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivateAccountWorker.class);
    @Qualifier("zeebeClientLifecycle")
    @Autowired
    private ZeebeClient zeebeClient;
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
    }
}
