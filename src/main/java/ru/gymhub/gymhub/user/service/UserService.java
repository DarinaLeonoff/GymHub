package ru.gymhub.gymhub.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.gymhub.gymhub.exceptions.NotFoundException;
import ru.gymhub.gymhub.user.UserRepository;
import ru.gymhub.gymhub.user.dto.UserMeDto;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserMeDto getMe(UserDetails user) {
        return userMapper.mapUserToMe(userRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found")));
    }
}
