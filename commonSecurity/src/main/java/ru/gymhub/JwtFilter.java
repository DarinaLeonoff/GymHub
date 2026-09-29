package ru.gymhub;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response,
                                    jakarta.servlet.FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {
            log.info("Authorization is {}", authHeader);
            filterChain.doFilter(request, response);
            return;
        }
//        получение непосредственно токена
        String token = authHeader.substring(7);
        try {
            UserPrincipal principal = jwtService.getPrincipal(token);
            JwtAuthToken auth = new JwtAuthToken(principal);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (JwtException e) {
            log.warn("Невалидный JWT токен: {}", e.getMessage());
            // Оставляем контекст пустым, Spring Security вернет 401 Unauthorized
        }

        filterChain.doFilter(request, response);
    }
}
