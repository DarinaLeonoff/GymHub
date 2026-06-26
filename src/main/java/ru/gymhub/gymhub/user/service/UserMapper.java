package ru.gymhub.gymhub.user.service;

import org.mapstruct.Mapper;
import ru.gymhub.gymhub.user.dto.UserMeDto;
import ru.gymhub.gymhub.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public UserMeDto mapUserToMe(User user);
}
