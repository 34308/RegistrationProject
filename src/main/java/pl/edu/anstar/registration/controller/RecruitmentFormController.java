package pl.edu.anstar.registration.controller;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.anstar.registration.ProcessConstants;
import pl.edu.anstar.registration.Services.UserTaskHandlerService;

import java.util.HashMap;
import java.util.Map;

import static io.camunda.zeebe.client.impl.Loggers.LOGGER;


@RestController
@RequestMapping("/")
public class RecruitmentFormController {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentFormController.class);
    private final ZeebeClient zeebeClient;

    @Autowired
    public RecruitmentFormController( @Qualifier("zeebeClientLifecycle") ZeebeClient client) {
        this.zeebeClient = client;
    }
    @PostMapping("/start")
    public void startProcessInstance() {

        LOG.info("Starting process " + ProcessConstants.BPMN_PROCESS_ID + " with variables: ");

        //Przykładowy obiekt użytkownika z polami z formularza
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "John");
        userData.put("surname", "Doe");
        userData.put("password", "zaq123!@K");
        userData.put("rePassword", "zaq123!@K");

        zeebeClient
                .newCreateInstanceCommand()
                .bpmnProcessId(ProcessConstants.BPMN_PROCESS_ID)
                .latestVersion()
                .variables(userData) //Przesłanie danych
                .send();
    }

}
