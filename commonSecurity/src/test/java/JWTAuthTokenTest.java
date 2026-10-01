import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.gymhub.JwtAuthToken;
import ru.gymhub.UserPrincipal;

public class JWTAuthTokenTest {

    private final UserPrincipal principal = new UserPrincipal(1L, "User", "User", "email@mail.ru");

    @Test
    void shouldMakeNewTokenSuccess() {
        JwtAuthToken token = new JwtAuthToken(principal);

        Assertions.assertNull(token.getCredentials());
        Assertions.assertEquals(principal, token.getPrincipal());
    }

    @Test
    void shouldThrowNullPointerIfPrincipalIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> new JwtAuthToken(null));
    }

}
