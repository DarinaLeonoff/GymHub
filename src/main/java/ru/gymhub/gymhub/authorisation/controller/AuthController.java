package ru.gymhub.gymhub.authorisation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.gymhub.gymhub.authorisation.dto.LoginRequest;
import ru.gymhub.gymhub.authorisation.dto.LoginResponse;
import ru.gymhub.gymhub.authorisation.dto.RegisterRequest;
import ru.gymhub.gymhub.authorisation.service.JWTService;
import ru.gymhub.gymhub.authorisation.service.RegistrationService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/registration")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        registrationService.register(request);
        System.out.println(request);
        return ResponseEntity.ok().build();
    }


    //todo replace logic to service lair
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());

        String token = jwtService.generateToken(user);

        return new LoginResponse(token);
    }

}
