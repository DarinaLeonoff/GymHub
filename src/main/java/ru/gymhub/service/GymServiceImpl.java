package ru.gymhub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gymhub.dto.request.CreateGymRequest;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.UpdateGymRequest;
import ru.gymhub.entity.Gym;
import ru.gymhub.exceptions.exceptions.GymNotFoundException;
import ru.gymhub.repository.GymRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class GymServiceImpl implements GymService {
  private final GymMapper mapper;
  private final GymRepository repository;

  @Override
  public GymDto createGym(CreateGymRequest dto) {
    return mapper.gymToDto(repository.save(mapper.createRequestToGym(dto)));
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
    if (id == 1) {
      log.info("Nice try. It is default gym that can't be deleted.");
      throw new IllegalArgumentException("It is default gym that can't be deleted.");
    }
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
