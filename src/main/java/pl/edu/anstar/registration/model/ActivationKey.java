package pl.edu.anstar.registration.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document("accountKeys")
public class ActivationKey {
    @Id
    private String userId;
    @Indexed(unique = true)
    private String value;
}
