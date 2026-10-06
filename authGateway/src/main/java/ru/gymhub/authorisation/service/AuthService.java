package ru.gymhub.authorisation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gymhub.authorisation.dto.LoginRequest;
import ru.gymhub.authorisation.dto.LoginResponse;
import ru.gymhub.authorisation.dto.RefreshTokenRequest;
import ru.gymhub.authorisation.dto.RegisterRequest;
import ru.gymhub.authorisation.entity.RefreshToken;
import ru.gymhub.authorisation.entity.User;
import ru.gymhub.authorisation.repository.RefreshRepository;
import ru.gymhub.authorisation.repository.UserRepository;
import ru.gymhub.exceptions.NotFoundException;
import ru.gymhub.exceptions.UserAlreadyExistsException;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshRepository refreshRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    public void register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User with this email already registered");
        }

        User user = mapper.registerRequestToUser(request);
        user.setActive(true);
        user.setCreated(LocalDateTime.now());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        log.info("Authorization success: {}", auth.isAuthenticated());

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new NotFoundException("User not " +
                "found. Authorization denied"));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        log.info("Access token is {}, refresh token is {}.", accessToken, refreshToken);

        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        RefreshToken token = refreshRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token is not in database or expired!"));

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        String newAccessToken = jwtService.generateAccessToken(user);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(requestRefreshToken)
                .build();
    }
}
