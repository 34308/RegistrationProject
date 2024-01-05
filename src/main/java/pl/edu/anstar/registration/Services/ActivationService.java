package pl.edu.anstar.registration.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.anstar.registration.exception.ActivationException;
import pl.edu.anstar.registration.model.ActivationKey;
import pl.edu.anstar.registration.model.User;
import pl.edu.anstar.registration.repository.ActivationKeyRepository;
import pl.edu.anstar.registration.repository.UserRepository;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivationService {
    private final ActivationKeyRepository activationKeyRepository;
    private final UserRepository userRepository;

    public String generateActivationKey(String email) {
        String activationKey = generateHmacSHA256Key();
        String userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new ActivationException("Can not generate key for not existing user"))
                .getId();
        ActivationKey activationKeyModel = new ActivationKey(userId, activationKey);
        activationKeyRepository.save(activationKeyModel);
        return activationKey;
    }

    private String generateHmacSHA256Key() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecureRandom secRandom = new SecureRandom();
            keyGenerator.init(secRandom);
            keyGenerator.generateKey();
            Key key = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(key.getEncoded()).replaceAll("([+?/])", "");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public void activateAccount(String activationKey) throws ActivationException{
        ActivationKey activationKeyModel = activationKeyRepository.findByValue(activationKey)
                .orElseThrow(() -> new ActivationException("There is no such activation key"));
        User userToActivate = userRepository.findById(activationKeyModel.getUserId())
                .orElseThrow(() -> new ActivationException("User with this activation key not exists"));
        if (userToActivate.isActive()) {
            throw new ActivationException("Can not activate activated user");
        }
        userToActivate.setActive(true);
        userRepository.save(userToActivate);
        activationKeyRepository.deleteByValue(activationKey);
    }

    public boolean checkIfAccountActive(String email) {
       Optional<User> userToCheckOptional = userRepository.findByEmail(email);
       if (userToCheckOptional.isEmpty()) {
           return false;
       }
       User userToCheck = userToCheckOptional.get();
       return userToCheck.isActive() && !activationKeyRepository.existsById(userToCheck.getId());
    }
}
