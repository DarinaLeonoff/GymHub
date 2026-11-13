package ru.gymhub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.LoginDto;
import ru.gymhub.dto.request.RegistrationRequest;
import ru.gymhub.entity.Gym;
import ru.gymhub.exceptions.exceptions.WrongPasswordException;
import ru.gymhub.exceptions.exceptions.EmailAlreadyRegisteredException;
import ru.gymhub.exceptions.exceptions.GymNotFoundException;
import ru.gymhub.repository.GymRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class GymAuthServiceImpl implements GymAuthService{
    private final GymMapper mapper;
    private final GymRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public GymDto createGym(RegistrationRequest request) {
        if(repository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyRegisteredException();
        }

        Gym gym = mapper.registrationRequestToEntity(request);
        gym.setPassHash(encoder.encode(request.getPassword()));
        return mapper.gymToDto(repository.save(gym));
    }

    @Override
    public GymDto login(LoginDto request) {
        Gym gym =
                repository.findByEmail(request.getEmail()).orElseThrow(() -> new GymNotFoundException(request.getEmail()));
        if(!encoder.matches(
                request.getPassword(),
                gym.getPassHash()
        )){
            log.info("Password {} was not matched to password from db", request.getPassword());
            throw new WrongPasswordException(request.getEmail());
        }
        return mapper.gymToDto(gym);
    }
}
