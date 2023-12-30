package pl.edu.anstar.registration.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.edu.anstar.registration.dto.UserDto;
import pl.edu.anstar.registration.exception.ValidationException;
import pl.edu.anstar.registration.model.User;

@Component
public class ValidateDataWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateDataWorker.class);


    @JobWorker(type = "validateData")
    public void validateData(final JobClient client, final ActivatedJob job) {
        try {
            UserDto userDto = job.getVariablesAsType(UserDto.class);

            if (!validateFields(userDto)) {
                throw new ValidationException("VALIDATION_FAILED", "Some fields are empty.");
            } else if (!isPasswordStrong(userDto.getPassword())) {
                throw new ValidationException("VALIDATION_FAILED", "Password doesn't meet our strength requirements.");
            }
        } catch (ValidationException e) {
            handleError(client, job, e.getMessage(), e.getErrorMessage());
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

    private boolean validateFields(UserDto userDto) {
        String name = userDto.getName();
        String surname = userDto.getSurname();
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        String rePassword = userDto.getRepeatPassword();

        boolean isNameNotEmpty = name != null && !name.isEmpty();
        boolean isSurnameNotEmpty = surname != null && !surname.isEmpty();
        boolean isEmailNotEmpty = email != null && !email.isEmpty();
        boolean isPasswordNotEmpty = password != null && !password.isEmpty();
        assert password != null;
        boolean doPasswordsMatch = password.equals(rePassword);

        return isNameNotEmpty && isSurnameNotEmpty && isPasswordNotEmpty && isEmailNotEmpty && doPasswordsMatch;
    }

    private boolean isPasswordStrong(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");
    }
}


