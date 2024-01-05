package pl.edu.anstar.registration.controller;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.anstar.registration.ProcessConstants;
import pl.edu.anstar.registration.Services.ActivationService;
import pl.edu.anstar.registration.Services.UserTaskHandlerService;
import pl.edu.anstar.registration.dto.ActivationResultDto;
import pl.edu.anstar.registration.dto.UserDto;
import pl.edu.anstar.registration.exception.ActivationException;
import pl.edu.anstar.registration.model.User;

import java.util.HashMap;
import java.util.Map;

import static io.camunda.zeebe.client.impl.Loggers.LOGGER;


@RestController
@RequestMapping("/")
public class RecruitmentFormController {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentFormController.class);
    private final ZeebeClient zeebeClient;
    private final ActivationService activationService;

    @Autowired
    public RecruitmentFormController(@Qualifier("zeebeClientLifecycle") ZeebeClient client, ActivationService activationService) {
        this.zeebeClient = client;
        this.activationService = activationService;
    }

    @PostMapping("/start")
    public void startProcessInstance(@RequestBody UserDto userDto) {

        LOG.info("Starting process " + ProcessConstants.BPMN_PROCESS_ID + " with variables: ");

        zeebeClient
                .newCreateInstanceCommand()
                .bpmnProcessId(ProcessConstants.BPMN_PROCESS_ID)
                .latestVersion()
                .variables(userDto) //Przesłanie obiektu User
                .send();
    }

    @GetMapping("/activate")
    public void activate(@RequestParam("k") String activationKey) {
        LOG.info("Reciving activation key " + activationKey);
        Map<String, Object> vars = new HashMap<>();
        vars.put("activationKey", activationKey);
        zeebeClient.newPublishMessageCommand().messageName("activation-key-received").correlationKey("activationKey")
                .variables(vars).send().join();
    }

}
