package pl.edu.anstar.registration.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import pl.edu.anstar.registration.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long deleteByEmail(String email);
}
