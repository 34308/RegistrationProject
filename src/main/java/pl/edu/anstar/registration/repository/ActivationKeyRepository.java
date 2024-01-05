package pl.edu.anstar.registration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import pl.edu.anstar.registration.model.ActivationKey;
import pl.edu.anstar.registration.model.User;

import java.util.Optional;

public interface ActivationKeyRepository extends MongoRepository<ActivationKey, String> {
    long deleteByValue(String value);

    @NonNull
    Optional<ActivationKey> findByValue(String value);
}
