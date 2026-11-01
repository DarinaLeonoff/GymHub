package ru.gymhub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;
import ru.gymhub.dto.CreateGymRequest;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.UpdateGymRequest;
import ru.gymhub.service.GymService;

@RestController
@RequestMapping("/gym/profile")
@RequiredArgsConstructor
public class GymProfileController {
  private final GymService service;

  @PostMapping
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
            ),
        @ApiResponse(
            responseCode = "401",
            description = "Пользователь не авторизован",
            content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "Доступ запрещен (недостаточно прав)",
            content = @Content)
      })
  public GymDto createGymInfo(@Valid @RequestBody CreateGymRequest dto) {
    return service.createGym(dto);
  }

  @PutMapping
  @Operation(
      summary = "Обновление информации о зале",
      description =
          "Позволяет обновить общую информацию о тренажерном зале (описание, контакты и т.д.).")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Информация успешно обновлена"),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные в запросе",
            content = @Content // Можно указать схему ошибки, если есть DTO
            ),
        @ApiResponse(
            responseCode = "401",
            description = "Пользователь не авторизован",
            content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "Доступ запрещен (недостаточно прав)",
            content = @Content)
      })
  public GymDto updateGymInfo(@Valid @RequestBody UpdateGymRequest dto)
      throws ChangeSetPersister.NotFoundException {
    //      default id = 1
    Long id = 1L;
    return service.updateGymInfo(id, dto);
  }

  // todo get profile

  // todo deactivate gym

}
