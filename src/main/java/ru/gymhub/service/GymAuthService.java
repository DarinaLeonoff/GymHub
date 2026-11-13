package ru.gymhub.service;

import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.LoginDto;
import ru.gymhub.dto.request.RegistrationRequest;

public interface GymAuthService {
    GymDto createGym(RegistrationRequest request);
    GymDto login(LoginDto request);
}
