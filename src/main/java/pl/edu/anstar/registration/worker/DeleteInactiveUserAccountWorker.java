package pl.edu.anstar.registration.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.edu.anstar.registration.Services.ActivationService;
import pl.edu.anstar.registration.Services.UserService;
import pl.edu.anstar.registration.dto.UserDto;
import pl.edu.anstar.registration.repository.ActivationKeyRepository;

@Component
@RequiredArgsConstructor
public class DeleteInactiveUserAccountWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveDataWorker.class);
    private final ActivationService activationService;
    @JobWorker(type = "deleteInnactiveUserAccount")
    public void saveData(final JobClient client, final ActivatedJob job) {
        try {
            UserDto userDto = job.getVariablesAsType(UserDto.class);
            activationService.deleteInactiveAccount(userDto.getEmail());
            LOGGER.info("Deleted user with email: " + userDto.getEmail());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
