package ru.gymhub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.LoginDto;
import ru.gymhub.dto.request.RegistrationRequest;
import ru.gymhub.dto.request.UpdateGymRequest;
import ru.gymhub.dto.response.LoginResponse;
import ru.gymhub.entity.Gym;
import ru.gymhub.exceptions.exceptions.EmailAlreadyRegisteredException;
import ru.gymhub.exceptions.exceptions.GymNotFoundException;
import ru.gymhub.exceptions.exceptions.WrongPasswordException;
import ru.gymhub.repository.GymRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class GymServiceImpl implements GymService {
  private final GymMapper mapper;
  private final GymRepository repository;
  private final PasswordEncoder encoder;
  private final AuthService authService;

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
  public LoginResponse login(LoginDto request) {
    Gym gym =
            repository.findByEmail(request.getEmail()).orElseThrow(() -> new GymNotFoundException(request.getEmail()));
    if(!encoder.matches(
            request.getPassword(),
            gym.getPassHash()
    )){
      throw new WrongPasswordException(request.getEmail());
    }

    String token = authService.generateToken(gym.getId());
    return  LoginResponse.builder()
            .accessToken(token)
            .tokenType("Bearer")
            .expiresIn(86400)
            .build();
  }

  @Override
  public GymDto updateGymInfo(Long id, UpdateGymRequest dto) {
    return mapper.gymToDto(repository.save(update(id, dto)));
  }

  @Override
  public GymDto getGymInfo(Long id) {
    return mapper.gymToDto(getById(id));
  }

  @Override
  public void deleteGym(Long id) {
    repository.deleteById(id);
  }

  private Gym update(Long id, UpdateGymRequest dto) {
    Gym gym = getById(id);

    String name = dto.getName();
    if (name != null && !gym.getName().equals(name)) {
      gym.setName(name);
    }

    String desc = dto.getDescription();
    if (desc != null && !gym.getDescription().equals(desc)) {
      gym.setDescription(desc);
    }

    String address = dto.getAddress();
    if (address != null && !gym.getAddress().equals(address)) {
      gym.setAddress(address);
    }

    String email = dto.getEmail();
    if (email != null && !gym.getEmail().equals(email)) {
      gym.setEmail(email);
    }

    String tel = dto.getTel();
    if (tel != null && !gym.getTel().equals(tel)) {
      gym.setTel(tel);
    }
    return gym;
  }

  private Gym getById(Long id) {
    return repository.findById(id).orElseThrow(() -> new GymNotFoundException(id));
  }

}
