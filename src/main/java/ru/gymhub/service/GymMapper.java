package ru.gymhub.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.gymhub.dto.request.CreateGymRequest;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.RegistrationRequest;
import ru.gymhub.entity.Gym;

@Mapper(componentModel = "spring")
public interface GymMapper {
  Gym createRequestToGym(CreateGymRequest dto);

  GymDto gymToDto(Gym gym);

  Gym registrationRequestToEntity(RegistrationRequest request);
}
