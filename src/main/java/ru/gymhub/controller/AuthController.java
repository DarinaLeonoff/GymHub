package ru.gymhub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.LoginDto;
import ru.gymhub.dto.request.RegistrationRequest;
import ru.gymhub.service.GymAuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final GymAuthService service;

    @PostMapping("/registration")
    @Operation(
            summary = "Создание профиля зала",
            description = "Позволяет создать аккаунт зала со всей базовой информацией")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Информация успешно обновлена"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные в запросе",
                            content = @Content // Можно указать схему ошибки, если есть DTO
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Аккаунт с такой почтой уже существует",
                            content = @Content
                    )
            })
    public GymDto createGym(@Valid @RequestBody RegistrationRequest dto) {
        return service.createGym(dto);
    }

    @PutMapping("/login")
    @Operation(
            summary = "Создание профиля зала",
            description = "Позволяет создать карточку зала со всей базовой информацией")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Информация успешно обновлена"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные в запросе",
                            content = @Content // Можно указать схему ошибки, если есть DTO
                    )
            })
    public GymDto login(@Valid @RequestBody LoginDto dto) {
        return service.login(dto);
    }
}
