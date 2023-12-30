package pl.edu.anstar.registration.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.anstar.registration.dto.UserDto;
import pl.edu.anstar.registration.model.User;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserMapper {
    public UserDto toDto(User user) {
        return UserDto.builder()
                .surname(user.getSurname())
                .password(user.getPassword())
                .name(user.getName())
                .build();
    }

    public User toEntity(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .surname(userDto.getSurname())
                .name(userDto.getName())
                .password(userDto.getPassword())
                .build();
    }
}
