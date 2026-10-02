package ru.gymhub;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class JWTService {
    private final String secret;

    public UserPrincipal getPrincipal(String token) {
        if(!isTokenValid(token)) {
            throw new JwtException("Invalid token");
        }

        Claims claims = extractClaims(token);

        String accountType = claims.get("account_type", String.class);
        String role = claims.get("role", String.class);
        String email = claims.get("email", String.class);
        long userId = Long.parseLong(claims.getSubject());

        return new UserPrincipal(userId, accountType, role, email);
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Ошибка валидации JWT токена: {}", e.getMessage());
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}