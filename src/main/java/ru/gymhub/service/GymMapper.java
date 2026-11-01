package ru.gymhub.service;

import org.mapstruct.Mapper;
import ru.gymhub.dto.CreateGymRequest;
import ru.gymhub.dto.GymDto;
import ru.gymhub.entity.Gym;

@Mapper(componentModel = "spring")
public interface GymMapper {
  Gym createRequestToGym(CreateGymRequest dto);

  GymDto gymToDto(Gym gym);
}
