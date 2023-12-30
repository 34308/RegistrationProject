package pl.edu.anstar.registration.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.edu.anstar.registration.Services.UserService;
import pl.edu.anstar.registration.dto.UserDto;
import pl.edu.anstar.registration.mapper.UserMapper;
import pl.edu.anstar.registration.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class SaveDataWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveDataWorker.class);
    private final UserService userService;

    @JobWorker(type = "saveData")
    public void saveData(final JobClient client, final ActivatedJob job) {
        try {
            UserDto userDto = job.getVariablesAsType(UserDto.class);

            LOGGER.info("Save user: " + userService.saveUser(userDto));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
