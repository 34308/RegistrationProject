package pl.edu.anstar.registration.dto;

import lombok.*;

//Pod baze danych na potem
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String repeatPassword;
}

