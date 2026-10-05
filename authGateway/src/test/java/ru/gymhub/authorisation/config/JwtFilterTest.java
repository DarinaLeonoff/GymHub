package ru.gymhub.authorisation.config;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.gymhub.authorisation.service.CustomUserDetailsService;
import ru.gymhub.authorisation.service.JWTService;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class JwtFilterTest {

    @Mock
    private JWTService jwtService;

    @Mock
    private CustomUserDetailsService userDetailsService;

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
    void doFilterNoAuthHeaderShouldContinueFilterChain() throws ServletException, IOException {
        jwtFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtService);
    }

    @Test
    @DisplayName("Если заголовок Authorization не начинается с Bearer — пропустить фильтрацию")
    void doFilterInvalidHeaderFormatShouldContinueFilterChain() throws ServletException, IOException {
        request.addHeader("Authorization", "Basic dXNlcjpwYXNz");

        jwtFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtService);
    }

    @Test
    @DisplayName("Если email = null продолжает без авторизации")
    void doFilterIfTokenWithEmailIsNull() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer dXNlcjpwYXNz");

        when(jwtService.extractUsername("dXNlcjpwYXNz")).thenReturn(null);

        jwtFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Если контекст не пуст продолжить цепочку смены контекста")
    void doFilterIfSecurityContextHolderNotEmpty() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer dXNlcjpwYXNz");
        SecurityContextHolder.getContext().setAuthentication(mock(Authentication.class));

        when(jwtService.extractUsername("dXNlcjpwYXNz")).thenReturn("user@mail.ru");

        jwtFilter.doFilter(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertNull(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    @DisplayName("Если токен не валидный продолжать цепочку без изменения контекста")
    void doFilterIfTokenIsNotValid()throws ServletException, IOException{
        request.addHeader("Authorization", "Bearer dXNlcjpwYXNz");

        when(jwtService.extractUsername("dXNlcjpwYXNz")).thenReturn("user@mail.ru");
        when(userDetailsService.loadUserByUsername("user@mail.ru")).thenReturn(org.springframework.security.core.userdetails.User
                .withUsername("user@mail.ru")
                .password("password")
                .disabled(false)
                .authorities(Collections.emptyList())
                .build());
        when(jwtService.isTokenValid(any(), any())).thenReturn(false);

        jwtFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }


    @Test
    @DisplayName("Если токен полностью валидный и пользователь найден")
    void doFilterSuccess()throws ServletException, IOException{
        request.addHeader("Authorization", "Bearer dXNlcjpwYXNz");

        when(jwtService.extractUsername("dXNlcjpwYXNz")).thenReturn("user@mail.ru");
        when(userDetailsService.loadUserByUsername("user@mail.ru")).thenReturn(org.springframework.security.core.userdetails.User
                .withUsername("user@mail.ru")
                .password("password")
                .disabled(false)
                .authorities(Collections.emptyList())
                .build());
        when(jwtService.isTokenValid(any(), any())).thenReturn(true);

        jwtFilter.doFilter(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(SecurityContextHolder.getContext().getAuthentication().getName(), "user@mail.ru");
    }

}
