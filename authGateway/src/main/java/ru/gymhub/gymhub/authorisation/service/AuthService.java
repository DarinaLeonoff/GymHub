package ru.gymhub.gymhub.authorisation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gymhub.gymhub.authorisation.dto.LoginRequest;
import ru.gymhub.gymhub.authorisation.dto.LoginResponse;
import ru.gymhub.gymhub.authorisation.dto.RefreshTokenRequest;
import ru.gymhub.gymhub.authorisation.dto.RegisterRequest;
import ru.gymhub.gymhub.authorisation.entity.RefreshToken;
import ru.gymhub.gymhub.authorisation.entity.User;
import ru.gymhub.gymhub.authorisation.repository.RefreshRepository;
import ru.gymhub.gymhub.authorisation.repository.UserRepository;
import ru.gymhub.gymhub.exceptions.NotFoundException;
import ru.gymhub.gymhub.exceptions.UserAlreadyExistsException;

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

        // 1. Ищем токен в Redis
        RefreshToken token = refreshRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token is not in database or expired!"));

        // 2. Ищем пользователя
        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // 3. Генерируем новый Access Token
        String newAccessToken = jwtService.generateAccessToken(user);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(requestRefreshToken)
                .build();
    }
}
