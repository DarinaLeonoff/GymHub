package registrationTests.Unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.gymhub.gymhub.authorisation.dto.RegisterRequest;
import ru.gymhub.gymhub.authorisation.service.AuthService;
import ru.gymhub.gymhub.authorisation.repository.UserRepository;
import ru.gymhub.gymhub.authorisation.entity.User;
import ru.gymhub.gymhub.authorisation.service.UserMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private AuthService registrationService;

    private RegisterRequest request = RegisterRequest.builder().email("user@test.com")
            .password("Password1!").build();

    @Test
    void shouldEncodePassword() {
        when(passwordEncoder.encode("Password1!")).thenReturn("encoded-password");
        when(mapper.registerRequestToUser(request)).thenReturn(mapRegisterDto(request));

        registrationService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("encoded-password", savedUser.getPasswordHash());
    }

    @Test
    void shouldBeSameEmail() {
        when(mapper.registerRequestToUser(request)).thenReturn(mapRegisterDto(request));
        registrationService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("user@test.com", savedUser.getEmail());
    }

    @Test
    void shouldBeActive() {
        when(mapper.registerRequestToUser(request)).thenReturn(mapRegisterDto(request));
        registrationService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals(savedUser.isActive(), true);
    }

    private User mapRegisterDto(RegisterRequest request){
        return User.builder()
                .email(request.getEmail())
                .passwordHash(request.getPassword())
                .created(LocalDateTime.now())
                .isActive(true)
                .accType(User.AccountType.CLIENT).role(User.RoleType.CLIENT)
                .build();
    }
}
