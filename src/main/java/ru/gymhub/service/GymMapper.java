package ru.gymhub.service;

import org.mapstruct.Mapper;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.RegistrationRequest;
import ru.gymhub.entity.Gym;

@Mapper(componentModel = "spring")
public interface GymMapper {
  GymDto gymToDto(Gym gym);

  Gym registrationRequestToEntity(RegistrationRequest request);
}
