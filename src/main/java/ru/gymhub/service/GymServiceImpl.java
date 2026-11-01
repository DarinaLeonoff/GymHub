package ru.gymhub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ru.gymhub.dto.CreateGymRequest;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.UpdateGymRequest;
import ru.gymhub.entity.Gym;
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
  public GymDto updateGymInfo(Long id, UpdateGymRequest dto)
      throws ChangeSetPersister.NotFoundException {
    log.info("updating gym: {}", dto.getName());
    GymDto gymDto = mapper.gymToDto(update(id, dto));
    log.info("updated name: {}", gymDto.getName());
    return gymDto;
  }

  private Gym update(Long id, UpdateGymRequest dto) throws ChangeSetPersister.NotFoundException {
    Gym gym = repository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);

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
}
