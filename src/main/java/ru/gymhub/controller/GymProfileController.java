package ru.gymhub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.UpdateGymRequest;
import ru.gymhub.service.GymService;

@RestController
@RequestMapping("/gym/profile")
@RequiredArgsConstructor
public class GymProfileController {
  private final GymService service;

  @PutMapping("/{id}")
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
  public GymDto updateGymInfo(@PathVariable Long id, @Valid @RequestBody UpdateGymRequest dto) {
    return service.updateGymInfo(id, dto);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Получение информации о зале",
      description =
          "Позволяет получить общую информацию о тренажерном зале (описание, контакты и т.д.).")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Информация успешно получена"),
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
  public GymDto getGymInfo(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
    return service.getGymInfo(id);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Удаление зала", description = "Позволяет удалить всю информацию о зале.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Информация успешно удалена"),
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
            content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Неверный аргумент(id). Попытка удалить дэфолтный зал.",
            content = @Content)
      })
  public void deleteGym(@PathVariable Long id) {
    service.deleteGym(id);
  }
}
