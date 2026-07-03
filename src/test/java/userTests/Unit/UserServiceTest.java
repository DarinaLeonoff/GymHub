package userTests.Unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import ru.gymhub.gymhub.exceptions.NotFoundException;
import ru.gymhub.gymhub.user.UserRepository;
import ru.gymhub.gymhub.user.dto.UserMeDto;
import ru.gymhub.gymhub.user.service.UserMapper;
import ru.gymhub.gymhub.user.service.UserService;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    private UserDetails user = User.builder()
            .username("test@mail.com")
            .password("password") // можно уже заэнкодить, если нужно
            .roles("USER")
            .build();

    @Test
    void getMeShouldThrowException(){
        when(repository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.getMe(user)
        );

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void getMeShouldReturnUserDto() {
        ru.gymhub.gymhub.user.entity.User userEntity = new ru.gymhub.gymhub.user.entity.User();
        userEntity.setEmail("test@mail.com");

        UserMeDto dto = new UserMeDto();

        when(repository.findByEmail(user.getUsername()))
                .thenReturn(Optional.of(userEntity));

        when(mapper.mapUserToMe(userEntity))
                .thenReturn(dto);

        UserMeDto result = service.getMe(user);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(dto);

        verify(repository).findByEmail("test@mail.com");
        verify(mapper).mapUserToMe(userEntity);
    }
}
