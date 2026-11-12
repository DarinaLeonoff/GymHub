package ru.gymhub.service;

import ru.gymhub.dto.request.CreateGymRequest;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.UpdateGymRequest;

public interface GymService {
  GymDto createGym(CreateGymRequest dto);

  GymDto updateGymInfo(Long id, UpdateGymRequest dto);

  GymDto getGymInfo(Long id);

  void deleteGym(Long id);
}
