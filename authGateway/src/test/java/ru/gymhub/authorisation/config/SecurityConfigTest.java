package ru.gymhub.authorisation.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.gymhub.authorisation.service.JWTService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class SecurityConfigTest {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JWTService jwtService;

    @TestConfiguration
    static class TestControllerConfig {
        @RestController
        static class TestController {
            @GetMapping("/api/v1/test/protected")
            public ResponseEntity<String> protectedEndpoint() {
                return ResponseEntity.ok("OK");
            }
        }
    }

    @Test
    @DisplayName("Публичные урлы должны быть доступны без авторизации (не 401)")
    void permitAllEndpoints_ShouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/v1/auth/non-existing-page"))
                .andExpect(status().isNotFound()); // Не 401, а 404, так как контроллер пропущен через Security

        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Защищенный урл без токена должен возвращать 401 Unauthorized")
    void protectedEndpoint_WithoutAuth_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v1/test/protected"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Защищенный урл с валидным пользователем должен возвращать 200 OK")
    @WithMockUser(username = "test@user.com", roles = "USER")
    void protectedEndpoint_WithAuth_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/test/protected"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PasswordEncoder должен успешно кодировать и проверять пароли")
    void passwordEncoder_ShouldEncodeAndMatch() {
        String rawPassword = "mySecretPassword123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }
}