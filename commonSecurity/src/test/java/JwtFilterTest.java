import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.gymhub.JWTService;
import ru.gymhub.JwtAuthToken;
import ru.gymhub.JwtFilter;
import ru.gymhub.UserPrincipal;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtFilterTest {

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private JwtFilter jwtFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(jakarta.servlet.FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Если заголовок Authorization отсутствует — передать управление дальше без аутентификации")
    void doFilter_NoAuthHeader_ShouldContinueFilterChain() throws ServletException, IOException {
        jwtFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtService);
    }

    @Test
    @DisplayName("Если заголовок Authorization не начинается с Bearer — пропустить фильтрацию")
    void doFilter_InvalidHeaderFormat_ShouldContinueFilterChain() throws ServletException, IOException {
        request.addHeader("Authorization", "Basic dXNlcjpwYXNz");

        jwtFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtService);
    }

    @Test
    @DisplayName("При валидном токене — успешно установить аутентификацию в SecurityContext")
    void doFilter_ValidToken_ShouldSetAuthentication() throws ServletException, IOException {
        String token = "valid.jwt.token";
        request.addHeader("Authorization", "Bearer " + token);

        UserPrincipal mockPrincipal = mock(UserPrincipal.class);
        when(mockPrincipal.role()).thenReturn("ROLE_USER");
        when(jwtService.getPrincipal(token)).thenReturn(mockPrincipal);

        jwtFilter.doFilter(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertTrue(authentication instanceof JwtAuthToken);
        assertEquals(mockPrincipal, authentication.getPrincipal());

        verify(jwtService, times(1)).getPrincipal(token);
    }

    @Test
    @DisplayName("При выбросе JwtException — контекст остается пустым, цепочка продолжается")
    void doFilter_InvalidToken_ShouldCatchExceptionAndContinue() throws ServletException, IOException {
        String token = "invalid.jwt.token";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtService.getPrincipal(token)).thenThrow(new JwtException("Invalid token signature"));

        jwtFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtService, times(1)).getPrincipal(token);
    }
}
