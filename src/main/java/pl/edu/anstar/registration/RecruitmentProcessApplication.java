package pl.edu.anstar.registration;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.anstar.registration.config.EnableMongoTestServer;

@SpringBootApplication
@EnableZeebeClient
@Deployment(resources = "classpath*:/model/*.*")
@EnableMongoTestServer
public class RecruitmentProcessApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecruitmentProcessApplication.class, args);
    }
}
