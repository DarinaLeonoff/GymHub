package ru.gymhub.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.LoginDto;
import ru.gymhub.dto.request.RegistrationRequest;
import ru.gymhub.entity.Gym;
import ru.gymhub.exceptions.exceptions.EmailAlreadyRegisteredException;
import ru.gymhub.exceptions.exceptions.GymNotFoundException;
import ru.gymhub.exceptions.exceptions.WrongPasswordException;
import ru.gymhub.repository.GymRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GymAuthServiceTest {

    @Mock
    private GymMapper mapper;

    @Mock
    private GymRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private GymAuthServiceImpl service;

    @Test
    void registrationGym_shouldSaveAndReturnDto() {
        RegistrationRequest request = new RegistrationRequest();
        Gym gymEntity = new Gym();
        Gym savedEntity = new Gym();
        GymDto expectedDto = new GymDto();

        when(mapper.registrationRequestToEntity(request)).thenReturn(gymEntity);
        when(repository.save(gymEntity)).thenReturn(savedEntity);
        when(mapper.gymToDto(savedEntity)).thenReturn(expectedDto);

        GymDto actualDto = service.createGym(request);

        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
        verify(repository, times(1)).save(gymEntity);
    }

    @Test
    void registrationGym_shouldThrowException() {
        RegistrationRequest request = RegistrationRequest.builder()
                .name("name")
                .description("desc")
                .address("address").tel("tel").email("email").password("password").build();

        when(repository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(EmailAlreadyRegisteredException.class, () -> service.createGym(request));
    }

    @Test
    void login_shouldReturnGymDto() {
        LoginDto request = LoginDto.builder()
                .email("email@mail.com")
                .password("password")
                .build();

        Gym gym = new Gym();
        gym.setEmail("email@mail.com");
        gym.setPassHash("hashedPassword");

        GymDto dto = new GymDto();

        when(repository.findByEmail("email@mail.com"))
                .thenReturn(Optional.of(gym));
        when(encoder.matches("password", "hashedPassword"))
                .thenReturn(true);
        when(mapper.gymToDto(gym)).thenReturn(dto);

        GymDto result = service.login(request);

        assertEquals(dto, result);

        verify(repository, times(1)).findByEmail("email@mail.com");
        verify(encoder, times(1)).matches("password", "hashedPassword");
        verify(mapper, times(1)).gymToDto(gym);
    }

    @Test
    void login_shouldThrowExceptionWhenEmailNotFound() {
        LoginDto request = LoginDto.builder()
                .email("unknown@mail.com")
                .password("password")
                .build();

        when(repository.findByEmail("unknown@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                GymNotFoundException.class,
                () -> service.login(request)
        );

        verify(repository, times(1)).findByEmail("unknown@mail.com");
        verify(encoder, never()).matches(anyString(), anyString());
    }

    @Test
    void login_shouldThrowExceptionWhenPasswordIsInvalid() {
        LoginDto request = LoginDto.builder()
                .email("email@mail.com")
                .password("wrongPassword")
                .build();

        Gym gym = new Gym();
        gym.setEmail("email@mail.com");
        gym.setPassHash("hashedPassword");

        when(repository.findByEmail("email@mail.com"))
                .thenReturn(Optional.of(gym));
        when(encoder.matches("wrongPassword", "hashedPassword"))
                .thenReturn(false);

        assertThrows(
                WrongPasswordException.class,
                () -> service.login(request)
        );

        verify(repository, times(1)).findByEmail("email@mail.com");
        verify(encoder, times(1)).matches("wrongPassword", "hashedPassword");
        verify(mapper, never()).gymToDto(any());
    }
}
