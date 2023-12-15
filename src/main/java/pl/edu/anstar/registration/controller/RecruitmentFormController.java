package pl.edu.anstar.registration.controller;

import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.anstar.registration.ProcessConstants;


@RestController
@RequestMapping("/")
public class RecruitmentFormController {

  private static final Logger LOG = LoggerFactory.getLogger(RecruitmentFormController.class);

  @Qualifier("zeebeClientLifecycle")
  @Autowired
  private ZeebeClient client;

  @PostMapping("/start")
  public void startProcessInstance() {

    LOG.info("Starting process " + ProcessConstants.BPMN_PROCESS_ID + " with variables: ");

    client
            .newCreateInstanceCommand()
            .bpmnProcessId(ProcessConstants.BPMN_PROCESS_ID)
            .latestVersion()
            .send();
  }
}
