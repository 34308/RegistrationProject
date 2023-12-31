package pl.edu.anstar.registration.model;

//import lombok.*;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document("users")
public class User {
    @Id
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private boolean isActive = false;
}

