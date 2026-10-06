package ru.gymhub.authorisation.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import ru.gymhub.authorisation.entity.User;
import ru.gymhub.authorisation.service.JWTService;


import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {
    private JWTService jwtService;

    // Секретный ключ должен быть длиной не менее 256 бит (32 символа) для HMAC-SHA
    private final String testSecret = "super-secret-key-that-is-at-least-32-bytes-long!";
    private final long accessExpirationMs = 900000L; // 15 минут
    private final long refreshExpirationMs = 604800000L; // 7 дней

    private User testUser;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
        ReflectionTestUtils.setField(jwtService, "secret", testSecret);
        ReflectionTestUtils.setField(jwtService, "accessExpirationMs", accessExpirationMs);
        ReflectionTestUtils.setField(jwtService, "refreshExpirationMs", refreshExpirationMs);

        testUser = User.builder()
                .id(100L)
                .email("test@gymhub.ru")
                .role(User.RoleType.CLIENT)
                .accType(User.AccountType.CLIENT)
                .build();

        userDetails = org.springframework.security.core.userdetails.User
                .withUsername("test@gymhub.ru")
                .password("password")
                .disabled(false)
                .authorities(Collections.emptyList())
                .build();
    }

    @Test
    @DisplayName("Генерация Access токена — токен не пустой и содержит корректный email")
    void generateAccessToken_ShouldCreateValidToken() {
        String token = jwtService.generateAccessToken(testUser);

        assertNotNull(token);
        assertFalse(token.isBlank());

        String extractedEmail = jwtService.extractUsername(token);
        assertEquals(testUser.getEmail(), extractedEmail);
    }

    @Test
    @DisplayName("Генерация Refresh токена — токен не пустой и валидный по подписи")
    void generateRefreshToken_ShouldCreateValidToken() {
        String refreshToken = jwtService.generateRefreshToken(testUser);

        assertNotNull(refreshToken);
        assertFalse(refreshToken.isBlank());
    }

    @Test
    @DisplayName("Проверка валидности токена — возвращает true при совпадении email и валидном сроке")
    void isTokenValid_ShouldReturnTrue_WhenEmailMatchesAndNotExpired() {
        String token = jwtService.generateAccessToken(testUser);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Проверка валидности токена — возвращает false при несовпадении email")
    void isTokenValid_ShouldReturnFalse_WhenEmailDoesNotMatch() {
        String token = jwtService.generateAccessToken(testUser);

        UserDetails wrongUser = org.springframework.security.core.userdetails.User
                .withUsername("wrong@gymhub.ru")
                .password("password")
                .disabled(false)
                .authorities(Collections.emptyList())
                .build();

        boolean isValid = jwtService.isTokenValid(token, wrongUser);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Истёкший токен — должен выбрасывать ExpiredJwtException")
    void isTokenExpired_ShouldThrowException_WhenTokenIsExpired() {
        // Устанавливаем время жизни 0 мс для теста истечения
        ReflectionTestUtils.setField(jwtService, "accessExpirationMs", -1000L);
        String expiredToken = jwtService.generateAccessToken(testUser);

        assertThrows(ExpiredJwtException.class, () -> jwtService.extractUsername(expiredToken));
    }

    @Test
    @DisplayName("Невалидный токен (битая строка) — выбрасывает MalformedJwtException")
    void extractUsername_ShouldThrowException_WhenTokenIsInvalid() {
        String invalidToken = "invalid.jwt.string";

        assertThrows(MalformedJwtException.class, () -> jwtService.extractUsername(invalidToken));
    }

    @Test
    @DisplayName("Токен, подписанный другим секретом — выбрасывает SignatureException")
    void extractUsername_ShouldThrowException_WhenSignatureIsInvalid() {
        JWTService wrongService = new JWTService();
        ReflectionTestUtils.setField(wrongService, "secret", "another-very-secret-key-that-is-32-bytes!");
        ReflectionTestUtils.setField(wrongService, "accessExpirationMs", accessExpirationMs);

        String tokenFromWrongSecret = wrongService.generateAccessToken(testUser);

        assertThrows(SignatureException.class, () -> jwtService.extractUsername(tokenFromWrongSecret));
    }
}
