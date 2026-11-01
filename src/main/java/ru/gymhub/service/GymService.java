package ru.gymhub.service;

import org.springframework.data.crossstore.ChangeSetPersister;
import ru.gymhub.dto.CreateGymRequest;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.UpdateGymRequest;

public interface GymService {
  GymDto createGym(CreateGymRequest dto);

  GymDto updateGymInfo(Long id, UpdateGymRequest dto) throws ChangeSetPersister.NotFoundException;
}
