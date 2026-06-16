package ru.gymhub.gymhub.authorisation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gymhub.gymhub.authorisation.dto.RegisterRequest;
import ru.gymhub.gymhub.user.UserRepository;
import ru.gymhub.gymhub.user.entity.User;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        userRepository.save(User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .build());
    }
}
