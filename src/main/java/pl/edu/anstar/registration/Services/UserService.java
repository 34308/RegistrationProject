package pl.edu.anstar.registration.Services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.anstar.registration.dto.UserDto;
import pl.edu.anstar.registration.mapper.UserMapper;
import pl.edu.anstar.registration.model.User;
import pl.edu.anstar.registration.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public User saveUser(UserDto userDto) {
        return userRepository.save(userMapper.toEntity(userDto));
    }
}
