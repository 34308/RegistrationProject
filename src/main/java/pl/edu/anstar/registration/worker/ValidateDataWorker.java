package pl.edu.anstar.registration.worker;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.edu.anstar.registration.model.User;

import java.util.Map;

@Component
public class ValidateDataWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateDataWorker.class);

    @JobWorker(type = "validateData")
    public void validateData(final JobClient client, final ActivatedJob job) {
        try {
            User user = job.getVariablesAsType(User.class);

            if (!validateFields(user)) {
                LOGGER.error("Some fields are empty.");
                client.newFailCommand(job.getKey())
                        .retries(job.getRetries() - 1)
                        .errorMessage("Some fields are empty.")
                        .send()
                        .join();
                throw new FieldEmptyException("Some fields are empty.");
            } else if(!isPasswordStrong(user.getPassword())){
                LOGGER.error("Password doesn't meet our strength requirements.");
                client.newFailCommand(job.getKey())
                        .retries(job.getRetries() - 1)
                        .errorMessage("Password doesn't meet our strength requirements.")
                        .send()
                        .join();
                throw new WeakPasswordException("Password doesn't meet our strength requirements.");
            }
        } catch (Exception e) {
            LOGGER.error("Exception occurred during data validation: {}", e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage("Unknown error")
                    .send()
                    .join();
        }
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
    public static class FieldEmptyException extends RuntimeException {
        public FieldEmptyException(String message) {
            super(message);
        }
    }

    public static class WeakPasswordException extends RuntimeException {
        public WeakPasswordException(String message) {
            super(message);
        }
    }
}


