import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.gymhub.UserPrincipal;

public class UserPrincipalTest {

    @Test
    void shouldCreatePrincipalAndReturnEmail(){
        UserPrincipal principal = new UserPrincipal(1L, "Type", "Role", "Email");

        Assertions.assertEquals("Email", principal.getName());
    }
}
