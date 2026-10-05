package service;

import org.junit.jupiter.api.Test;
import ru.gymhub.authorisation.dto.RegisterRequest;
import ru.gymhub.authorisation.entity.User;
import ru.gymhub.authorisation.service.UserMapperImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserMapperTest {

    private UserMapperImpl userMapper = new UserMapperImpl();

    @Test
    void shouldReturnNullIfRequestIsNull() {
        User user = userMapper.registerRequestToUser(null);

        assertNull(user);
    }

    @Test
    void shouldReturnUser() {
        RegisterRequest request =
                RegisterRequest.builder().email("user@email.com").password("userPass").accType(User.AccountType.CLIENT).role(User.RoleType.CLIENT).build();

        User user = userMapper.registerRequestToUser(request);

        assertEquals(request.getEmail(), user.getEmail());
        assertEquals(request.getAccType(), user.getAccType());
        assertEquals(request.getRole(), user.getRole());
    }
}
