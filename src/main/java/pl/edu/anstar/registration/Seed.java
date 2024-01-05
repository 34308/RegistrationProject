package pl.edu.anstar.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.edu.anstar.registration.model.User;
import pl.edu.anstar.registration.repository.UserRepository;

import java.util.List;

//@Component
//@RequiredArgsConstructor
//public class Seed implements CommandLineRunner {
//    private final UserRepository userRepository;
//
//    @Override
//    public void run(String... args) {
//        seedData();
//    }
//
//    private void seedData() {
//        if (userRepository.count() > 0) {
//            return;
//        }
//        List<User> users = List.of(
//                User.builder()
//                        .email("andrzej.duda@gmail.com")
//                        .name("Andrzej")
//                        .surname("Duda")
//                        .password("prezydent123")
//                        .build(),
//                User.builder()
//                        .email("donald.tusk@gmail.com")
//                        .name("Donald")
//                        .surname("Tusk")
//                        .password("premier123")
//                        .build(),
//                User.builder()
//                        .email("kazimierz.tetmeier@gmail.com")
//                        .name("Kazimierz")
//                        .surname("Tetmeier")
//                        .password("przerwa123")
//                        .build()
//        );
//        userRepository.saveAll(users);
//    }
//}
