package ru.gymhub.authorisation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.gymhub.authorisation.dto.LoginRequest;
import ru.gymhub.authorisation.dto.LoginResponse;
import ru.gymhub.authorisation.dto.RefreshTokenRequest;
import ru.gymhub.authorisation.dto.RegisterRequest;
import ru.gymhub.authorisation.entity.User;
import ru.gymhub.authorisation.service.AuthService;
import ru.gymhub.authorisation.service.JWTService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JWTService jwtService;

    // ==========================================
    // 1. ТЕСТЫ РЕГИСТРАЦИИ (/registration)
    // ==========================================
    @Nested
    @DisplayName("POST /api/v1/auth/registration")
    class RegistrationTests {

        @Test
        @DisplayName("Успешная регистрация — возвращает 200 OK")
        void register_Success_ShouldReturn200() throws Exception {
            RegisterRequest request = new RegisterRequest("test@gymhub.ru", "passworD!123", User.AccountType.CLIENT,
                    User.RoleType.CLIENT);

            doNothing().when(authService).register(any(RegisterRequest.class));

            mockMvc.perform(post("/api/v1/auth/registration")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(authService, times(1)).register(any(RegisterRequest.class));
        }

        @Test
        @DisplayName("Ошибка валидации тела запроса — возвращает 400 Bad Request")
        void register_InvalidBody_ShouldReturn400() throws Exception {
            // Передаем некорректные данные (например, пустой email/пароль)
            RegisterRequest invalidRequest = new RegisterRequest("", "", null, null);

            mockMvc.perform(post("/api/v1/auth/registration")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authService);
        }
    }

    // ==========================================
    // 2. ТЕСТЫ АУТЕНТИФИКАЦИИ (/login)
    // ==========================================
    @Nested
    @DisplayName("POST /api/v1/auth/login")
    class LoginTests {

        @Test
        @DisplayName("Успешный вход — возвращает 200 OK и LoginResponse")
        void login_Success_ShouldReturn200AndResponse() throws Exception {
            LoginRequest request = new LoginRequest();
            request.setEmail("test@gymhub.ru");
            request.setPassword("password123");

            LoginResponse expectedResponse = new LoginResponse("access.token.here", "refresh.token.here");

            when(authService.login(any(LoginRequest.class))).thenReturn(expectedResponse);

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("access.token.here"))
                    .andExpect(jsonPath("$.refreshToken").value("refresh.token.here"));

            verify(authService, times(1)).login(any(LoginRequest.class));
        }

        @Test
        @DisplayName("Невалидный LoginRequest — возвращает 400 Bad Request")
        void login_InvalidRequest_ShouldReturn400() throws Exception {
            LoginRequest invalidRequest = new LoginRequest();

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authService);
        }
    }

    // ==========================================
    // 3. ТЕСТЫ ОБНОВЛЕНИЯ ТОКЕНА (/refresh)
    // ==========================================
    @Nested
    @DisplayName("POST /api/v1/auth/refresh")
    class RefreshTests {

        @Test
        @DisplayName("Успешное обновление токена — возвращает 200 OK")
        void refresh_Success_ShouldReturn200AndResponse() throws Exception {
            RefreshTokenRequest request = new RefreshTokenRequest("valid.refresh.token");
            LoginResponse expectedResponse = new LoginResponse("new.access.token", "new.refresh.token");

            when(authService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(expectedResponse);

            mockMvc.perform(post("/api/v1/auth/refresh")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("new.access.token"))
                    .andExpect(jsonPath("$.refreshToken").value("new.refresh.token"));

            verify(authService, times(1)).refreshToken(any(RefreshTokenRequest.class));
        }
    }
}