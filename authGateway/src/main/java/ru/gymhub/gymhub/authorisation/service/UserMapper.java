package ru.gymhub.gymhub.authorisation.service;

import org.mapstruct.Mapper;
import ru.gymhub.gymhub.authorisation.dto.RegisterRequest;
import ru.gymhub.gymhub.authorisation.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
   User registerRequestToUser(RegisterRequest req);
}
