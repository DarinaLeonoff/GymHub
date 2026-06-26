package ru.gymhub.gymhub.user.controller;

import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gymhub.gymhub.user.dto.UserMeDto;
import ru.gymhub.gymhub.user.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public UserMeDto getMe(@AuthenticationPrincipal UserDetails user){
        return userService.getMe(user);
    }
}
