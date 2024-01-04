package pl.edu.anstar.registration.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.edu.anstar.registration.Services.UserService;
import pl.edu.anstar.registration.dto.UserDto;

@Component
@RequiredArgsConstructor
public class VerifyDataWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyDataWorker.class);
    private final UserService userService;

    @JobWorker(type = "verifyData")
    public void verifyData(final JobClient client, final ActivatedJob job) {
        UserDto userDto = job.getVariablesAsType(UserDto.class);
        boolean userWithEmailExists = userService.userWithThisEmailExists(userDto.getEmail());
        if (userWithEmailExists){
            handleError(client, job, "VERIFICATION_FAILED", "User with this email already exists");
        }
        LOGGER.info("verify data");
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
