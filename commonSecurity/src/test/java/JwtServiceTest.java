import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.gymhub.JWTService;
import ru.gymhub.UserPrincipal;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtServiceTest {
    private String secret = "secretKeyForTestsecretKeyForTestsecretKeyForTestsecretKeyForTestsecretKeyForTestsecretKeyForTestsecretKeyForTestsecretKeyForTest";

    private final JWTService service = new JWTService(secret);

    @Test
    void ShouldThrowJWTException(){
        Assertions.assertThrows(JwtException.class,
                () -> service.getPrincipal(generateTestToken(new Date(System.currentTimeMillis() - 60_000))));
    }

    @Test
    void shouldReturnPrincipal(){
        UserPrincipal principal = service.getPrincipal(generateTestToken(new Date(System.currentTimeMillis() + 60_000)));

        Assertions.assertEquals("user@user", principal.getName());
        Assertions.assertEquals("user", principal.accountType());
        Assertions.assertEquals("user", principal.role());
        Assertions.assertEquals(1L, principal.userId());
    }

    private String generateTestToken(Date expiration) {
        return Jwts.builder().subject("1").claim("account_type", "user").claim("role", "user").claim("email", "user" +
                "@user").issuedAt(new Date()).expiration(expiration).signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))).compact();
    }
}
