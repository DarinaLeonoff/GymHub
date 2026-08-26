package registrationTests.Unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.gymhub.gymhub.authorisation.service.CustomUserDetailsService;
import ru.gymhub.gymhub.authorisation.repository.UserRepository;
import ru.gymhub.gymhub.authorisation.entity.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    @Test
    void shouldLoadUserByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(
                User.builder().id(1L).email("email").passwordHash("password cash").isActive(false).accType(User.AccountType.CLIENT).role(User.RoleType.CLIENT).build()));
        UserDetails user = service.loadUserByUsername("email");

        assertEquals(user.getPassword(), "password cash");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("unknown@test.com")
        );
    }
}
