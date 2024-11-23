package com.gmoi.directmessage.entities.user;

import com.gmoi.directmessage.properties.UserProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProperties userProperties;

    @Bean
    public CommandLineRunner initAdminUser() {
        return _ -> {
            Optional<User> adminUser = userRepository.findByEmail(userProperties.getAdmin().getEmail());
            if (adminUser.isEmpty()) {
                User user = new User();
                user.setEmail(userProperties.getAdmin().getEmail());
                user.setPassword(passwordEncoder.encode(userProperties.getAdmin().getPassword()));
                user.setRole(UserRole.ADMIN);
                userRepository.save(user);
                log.info("Admin user created!");
            }
        };
    }
}
