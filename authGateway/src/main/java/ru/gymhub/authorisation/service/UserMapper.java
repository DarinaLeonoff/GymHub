package ru.gymhub.authorisation.service;

import org.mapstruct.Mapper;
import ru.gymhub.authorisation.dto.RegisterRequest;
import ru.gymhub.authorisation.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
   User registerRequestToUser(RegisterRequest req);
}
