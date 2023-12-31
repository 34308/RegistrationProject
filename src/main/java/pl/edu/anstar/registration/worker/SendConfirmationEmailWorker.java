package pl.edu.anstar.registration.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.edu.anstar.registration.Services.ActivationService;
import pl.edu.anstar.registration.Services.MailService;
import pl.edu.anstar.registration.Services.UserService;
import pl.edu.anstar.registration.dto.UserDto;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SendConfirmationEmailWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendConfirmationEmailWorker.class);
    private final ActivationService activationService;
    @JobWorker(type = "sendConfirmationEmail")
    public void sendConfirmationEmail(final JobClient client, final ActivatedJob job) throws IOException, EmailException {
        UserDto userDto = job.getVariablesAsType(UserDto.class);
        String title = "Activation email freom gistration service for:"+ userDto.getName();
        String activationKey = activationService.generateActivationKey(userDto.getEmail());
        String activationUrl = "http://localhost:8080/activate?k=" + activationKey;
        String message = """
                
                Click this link to activate account
                *** ACTIVATION LINK ***
                
                %s
                
                ***
                """.formatted(activationUrl);

        //MailService.sendEmail(userDto.getEmail(),title,message);
        LOGGER.info(message);
    }
}
