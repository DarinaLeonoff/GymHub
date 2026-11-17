package ru.gymhub.service;

import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.LoginDto;
import ru.gymhub.dto.request.RegistrationRequest;
import ru.gymhub.dto.request.UpdateGymRequest;

public interface GymService {
    GymDto createGym(RegistrationRequest request);

    GymDto login(LoginDto request);

    GymDto updateGymInfo(Long id, UpdateGymRequest dto);

  GymDto getGymInfo(Long id);

  void deleteGym(Long id);
}
