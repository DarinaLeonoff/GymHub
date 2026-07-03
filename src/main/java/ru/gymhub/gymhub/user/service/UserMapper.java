package ru.gymhub.gymhub.user.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.gymhub.gymhub.authorisation.dto.RegisterRequest;
import ru.gymhub.gymhub.user.dto.UserMeDto;
import ru.gymhub.gymhub.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public UserMeDto mapUserToMe(User user);

    public User mapRegisterDtoToUser(RegisterRequest user);
}
