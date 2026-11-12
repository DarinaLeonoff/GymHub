package ru.gymhub.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;
import ru.gymhub.dto.request.CreateGymRequest;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.UpdateGymRequest;
import ru.gymhub.entity.Gym;
import ru.gymhub.exceptions.exceptions.GymNotFoundException;
import ru.gymhub.repository.GymRepository;

@ExtendWith(MockitoExtension.class)
public class GymServiceTest {
  @Mock private GymMapper mapper;

  @Mock private GymRepository repository;

  @InjectMocks private GymServiceImpl gymService;

  // --- Тесты для удаления (deleteGym) ---

  @Test
  void deleteGym_whenIdIsOne_shouldThrowExceptionAndNotDelete() {
    // Act & Assert: проверяем, что выбрасывается IllegalArgumentException
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> gymService.deleteGym(1L));

    assertEquals("It is default gym that can't be deleted.", exception.getMessage());

    // Убедимся, что метод удаления репозитория НИКОГДА не вызывался
    verify(repository, never()).deleteById(any());
  }

  @Test
  void deleteGym_whenIdIsOther_shouldCallRepositoryDelete() {
    Long gymId = 2L;

    // Act
    gymService.deleteGym(gymId);

    // Assert: проверяем, что репозиторий попытался удалить этот id
    verify(repository, times(1)).deleteById(gymId);
  }

  // --- Тесты для создания (createGym) ---

  @Test
  void createGym_shouldSaveAndReturnDto() {
    CreateGymRequest request = new CreateGymRequest();
    Gym gymEntity = new Gym();
    Gym savedEntity = new Gym();
    GymDto expectedDto = new GymDto();

    when(mapper.createRequestToGym(request)).thenReturn(gymEntity);
    when(repository.save(gymEntity)).thenReturn(savedEntity);
    when(mapper.gymToDto(savedEntity)).thenReturn(expectedDto);

    GymDto actualDto = gymService.createGym(request);

    assertNotNull(actualDto);
    assertEquals(expectedDto, actualDto);
    verify(repository, times(1)).save(gymEntity);
  }

  // --- Тесты для получения информации (getGymInfo) ---

  @Test
  void getGymInfo_whenGymExists_shouldReturnDto() throws Exception {
    Long gymId = 2L;
    Gym gym = new Gym();
    GymDto expectedDto = new GymDto();

    when(repository.findById(gymId)).thenReturn(Optional.of(gym));
    when(mapper.gymToDto(gym)).thenReturn(expectedDto);

    GymDto actualDto = gymService.getGymInfo(gymId);

    assertNotNull(actualDto);
    assertEquals(expectedDto, actualDto);
    verify(repository, times(1)).findById(gymId);
  }

  @Test
  void getGymInfo_whenGymNotFound_shouldThrowNotFoundException() {
    Long gymId = 99L;

    when(repository.findById(gymId)).thenReturn(Optional.empty());

    assertThrows(
        GymNotFoundException.class,
        () -> {
          gymService.getGymInfo(gymId);
        });

    verify(repository, times(1)).findById(gymId);
  }

  // --- Тесты для обновления информации (updateGymInfo) ---

  @Test
  void updateGym_shouldSaveAndReturnDto() throws ChangeSetPersister.NotFoundException {
    UpdateGymRequest request =
        UpdateGymRequest.builder()
            .name("1name")
            .description("1desc")
            .address("1address")
            .tel("1tel")
            .email("1email")
            .build();
    Gym gymEntity = new Gym(1L, "name", "desc", "address", "tel", "email");
    GymDto expectedDto =
        new GymDto(
            gymEntity.getId(),
            gymEntity.getName(),
            gymEntity.getDescription(),
            gymEntity.getAddress(),
            gymEntity.getTel(),
            gymEntity.getEmail());

    when(repository.findById(anyLong())).thenReturn(Optional.of(gymEntity));
    when(repository.save(gymEntity)).thenReturn(gymEntity);
    when(mapper.gymToDto(gymEntity)).thenReturn(expectedDto);

    GymDto actualDto = gymService.updateGymInfo(1L, request);

    assertNotNull(actualDto);
    assertEquals(expectedDto, actualDto);
    verify(repository, times(1)).save(gymEntity);
  }

  @Test
  void updateGym_ifNoChanges_shouldSaveAndReturnDto() throws ChangeSetPersister.NotFoundException {
    UpdateGymRequest request = new UpdateGymRequest();
    Gym gymEntity = new Gym();
    GymDto expectedDto = new GymDto();

    when(repository.findById(anyLong())).thenReturn(Optional.of(gymEntity));
    when(repository.save(gymEntity)).thenReturn(gymEntity);
    when(mapper.gymToDto(gymEntity)).thenReturn(expectedDto);

    GymDto actualDto = gymService.updateGymInfo(1L, request);

    assertNotNull(actualDto);
    assertEquals(expectedDto, actualDto);
    verify(repository, times(1)).save(gymEntity);
  }

  @Test
  void updateGym_ifSameFieldsContent_shouldSaveAndReturnDto()
      throws ChangeSetPersister.NotFoundException {
    UpdateGymRequest request =
        UpdateGymRequest.builder()
            .name("name")
            .description("desc")
            .address("address")
            .tel("tel")
            .email("email")
            .build();
    Gym gymEntity = new Gym(1L, "name", "desc", "address", "tel", "email");
    GymDto expectedDto =
        new GymDto(
            gymEntity.getId(),
            gymEntity.getName(),
            gymEntity.getDescription(),
            gymEntity.getAddress(),
            gymEntity.getTel(),
            gymEntity.getEmail());

    when(repository.findById(anyLong())).thenReturn(Optional.of(gymEntity));
    when(repository.save(gymEntity)).thenReturn(gymEntity);
    when(mapper.gymToDto(gymEntity)).thenReturn(expectedDto);

    GymDto actualDto = gymService.updateGymInfo(1L, request);

    assertNotNull(actualDto);
    assertEquals(expectedDto, actualDto);
    verify(repository, times(1)).save(gymEntity);
  }
}
