package ru.gymhub.service;

import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.UpdateGymRequest;

public interface GymService {
  GymDto updateGymInfo(Long id, UpdateGymRequest dto);

  GymDto getGymInfo(Long id);

  void deleteGym(Long id);
}
