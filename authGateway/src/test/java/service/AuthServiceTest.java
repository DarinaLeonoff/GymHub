package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import ru.gymhub.authorisation.dto.LoginRequest;
import ru.gymhub.authorisation.dto.LoginResponse;
import ru.gymhub.authorisation.dto.RefreshTokenRequest;
import ru.gymhub.authorisation.dto.RegisterRequest;
import ru.gymhub.authorisation.entity.RefreshToken;
import ru.gymhub.authorisation.entity.User;
import ru.gymhub.authorisation.repository.RefreshRepository;
import ru.gymhub.authorisation.repository.UserRepository;
import ru.gymhub.authorisation.service.AuthService;
import ru.gymhub.authorisation.service.JWTService;
import ru.gymhub.exceptions.UserAlreadyExistsException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @Mock
    private RefreshRepository refreshRepository;

    @InjectMocks
    private AuthService service;

    User existingUser = User.builder()
            .id(1L)
            .email("email@example.com")
            .passwordHash("password cash")
            .isActive(false)
            .accType(User.AccountType.CLIENT)
            .role(User.RoleType.CLIENT)
            .build();

    @Test
    @DisplayName("Если при регистрации имэйл был найден в базе зарегистрированных пользователей должно быть выброшено" +
            " исключение")
    void shouldThrowIfEmailWasRegistered() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("email@example.com");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(UserAlreadyExistsException.class, () -> service.register(request));
    }

    @Test
    @DisplayName("При успешной авторизации возвращается объект с двумя токенами")
    void shouldReturnLoginResponseIfLogInSuccess() {
        LoginRequest request = new LoginRequest();
        request.setEmail("Email");
        request.setPassword("Password");

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()))).thenReturn(mock(Authentication.class));

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        when(jwtService.generateAccessToken(existingUser)).thenReturn("AccessToketCreated");
        when(jwtService.generateRefreshToken(existingUser)).thenReturn("RefreshToketCreated");

        LoginResponse response = service.login(request);
        assertEquals("AccessToketCreated", response.getAccessToken());
        assertEquals("RefreshToketCreated", response.getRefreshToken());
    }

    @Test
    @DisplayName("При успешном обновления токена должен вернуться объект с двумя токенами")
    void shouldReturnLoginResponseIfSuccessRefresh(){
        RefreshTokenRequest request = new RefreshTokenRequest("RefreshToken");

        when(refreshRepository.findByToken(request.getRefreshToken())).thenReturn(Optional.of(RefreshToken.builder().id(
                "id").userId(1L).token("RefreshToken").ttlInSeconds(60000L).build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(jwtService.generateAccessToken(any())).thenReturn("NewAccess");

        LoginResponse response = service.refreshToken(request);
        assertEquals("NewAccess", response.getAccessToken());
        assertEquals("RefreshToken", response.getRefreshToken());
    }
}
