package pl.edu.anstar.registration.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.edu.anstar.registration.exception.ValidationException;
import pl.edu.anstar.registration.model.User;

@Component
public class ValidateDataWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateDataWorker.class);


    @JobWorker(type = "validateData")
    public void validateData(final JobClient client, final ActivatedJob job) {
        try {
            User user = job.getVariablesAsType(User.class);

            if (!validateFields(user)) {
                throw new ValidationException("VALIDATION_FAILED", "Some fields are empty.");
            } else if (!isPasswordStrong(user.getPassword())) {
                throw new ValidationException("VALIDATION_FAILED", "Password doesn't meet our strength requirements.");
            }

            // Do something with the valid data

            client.newCompleteCommand(job.getKey()).send().join();
        } catch (ValidationException e) {
            handleError(client, job, e.getMessage(), e.getErrorMessage());
        } catch (Exception e) {
            handleUnknownException(client, job);
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

    private void handleUnknownException(JobClient client, ActivatedJob job) {
        client.newFailCommand(job.getKey())
                .retries(job.getRetries() - 1)
                .errorMessage("Unknown error")
                .send()
                .join();
    }
    private boolean validateFields(User user) {
        String name = user.getName();
        String surname = user.getSurname();
        String email = user.getEmail();
        String password = user.getPassword();
        //String rePassword = user.getRePassword();

        boolean isNameNotEmpty = name != null && !name.isEmpty();
        boolean isSurnameNotEmpty = surname != null && !surname.isEmpty();
        boolean isEmailNotEmpty = email != null && !email.isEmpty();
        boolean isPasswordNotEmpty = password != null && !password.isEmpty();
        //boolean doPasswordsMatch = password.equals(rePassword);

        return isNameNotEmpty && isSurnameNotEmpty && isPasswordNotEmpty && isEmailNotEmpty;
    }

    private boolean isPasswordStrong(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");
    }
}


