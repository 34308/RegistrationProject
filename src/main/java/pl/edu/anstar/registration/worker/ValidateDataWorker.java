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

import java.util.Map;

@Component
public class ValidateDataWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateDataWorker.class);

    @JobWorker(type = "validateData")
    public void validateData(final JobClient client, final ActivatedJob job) {
        try {
            Map<String, Object> variables = job.getVariablesAsMap();

            //Wyłuskanie zmiennych
            String name = (String) variables.get("name");
            String surname = (String) variables.get("surname");
            String password = (String) variables.get("password");
            String rePassword = (String) variables.get("rePassword");

            boolean isDataValid = validateUserData(name, surname, password, rePassword);

            if (isDataValid) {
                LOGGER.info("User data is valid.");
                client.newCompleteCommand(job.getKey()).send().join();
            } else {
                // Regex czy hasło jest silne
                boolean isPasswordStrong = password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");
                if (!isPasswordStrong) {
                    LOGGER.warn("Password does not meet strength requirements.");
                } else {
                    LOGGER.warn("User data is invalid.");
                }
                client.newFailCommand(job.getKey()).retries(job.getRetries() - 1).errorMessage("Invalid user data").send().join();
            }
        } catch (Exception e) {
            LOGGER.error("Exception occurred during data validation: {}", e.getMessage());
        }
    }

    //Sprawdza czy jakieś pola nie są puste
    private boolean validateUserData(String name, String surname, String password, String rePassword) {
        boolean isNameValid = name != null && !name.isEmpty();
        boolean isSurnameValid = surname != null && !surname.isEmpty();
        boolean isPasswordsMatch = password.equals(rePassword);

        return isNameValid && isSurnameValid && isPasswordsMatch;
    }
}

