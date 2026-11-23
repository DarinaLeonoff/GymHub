package ru.gymhub.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.LoginDto;
import ru.gymhub.dto.request.RegistrationRequest;

public interface AuthService {
    String generateToken(Long gymId);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);

}
