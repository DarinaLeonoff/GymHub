package ru.gymhub.gymhub.authorisation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gymhub.gymhub.authorisation.dto.RegisterRequest;
import ru.gymhub.gymhub.exceptions.UserAlreadyExistsException;
import ru.gymhub.gymhub.user.UserRepository;
import ru.gymhub.gymhub.user.entity.User;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        if(!userRepository.findByEmail(request.getEmail()).isEmpty()){
            throw new UserAlreadyExistsException("User with this email already registered");
        }
        userRepository.save(User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .build());
    }
}
