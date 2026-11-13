package ru.gymhub.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.gymhub.dto.request.CreateGymRequest;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.RegistrationRequest;
import ru.gymhub.entity.Gym;

public class GymMapperTest {
  private GymMapperImpl mapper = new GymMapperImpl();

  @Test
  void createRequestToGym_shouldReturnNullIfRequestNull() {
    CreateGymRequest request = null;

    Gym gym = mapper.createRequestToGym(request);

    Assertions.assertNull(gym);
  }

  @Test
  void createRequestToGym_shouldConvertRequestToGym() {
    CreateGymRequest request =
        CreateGymRequest.builder()
            .name("name")
            .description("desc")
            .address("address")
            .tel("tel")
            .email("email")
            .build();

    Gym gym = mapper.createRequestToGym(request);

    Assertions.assertEquals(request.getName(), gym.getName());
    Assertions.assertEquals(request.getDescription(), gym.getDescription());
    Assertions.assertEquals(request.getAddress(), gym.getAddress());
    Assertions.assertEquals(request.getTel(), gym.getTel());
    Assertions.assertEquals(request.getEmail(), gym.getEmail());
  }

  @Test
  void gymToDto_shouldReturnNullIfRequestNull() {
    Gym gym = null;

    GymDto dto = mapper.gymToDto(gym);

    Assertions.assertNull(dto);
  }

  @Test
  void gymToDto_shouldConvertGymToDto() {
    Gym gym = new Gym(1L, "name", "desc", "address", "tel", "email", "passHash");

    GymDto dto = mapper.gymToDto(gym);

    Assertions.assertEquals(gym.getName(), dto.getName());
    Assertions.assertEquals(gym.getDescription(), dto.getDescription());
    Assertions.assertEquals(gym.getAddress(), dto.getAddress());
    Assertions.assertEquals(gym.getTel(), dto.getTel());
    Assertions.assertEquals(gym.getEmail(), dto.getEmail());
  }

  @Test
  void registrationRequestToGym_shouldConvertRequestToGym() {
    RegistrationRequest request =
            RegistrationRequest.builder()
                    .name("name")
                    .description("desc")
                    .address("address")
                    .tel("tel")
                    .email("email")
                    .password("pass12@Word")
                    .build();

    Gym gym = mapper.registrationRequestToEntity(request);

    Assertions.assertEquals(request.getName(), gym.getName());
    Assertions.assertEquals(request.getDescription(), gym.getDescription());
    Assertions.assertEquals(request.getAddress(), gym.getAddress());
    Assertions.assertEquals(request.getTel(), gym.getTel());
    Assertions.assertEquals(request.getEmail(), gym.getEmail());
    Assertions.assertNull(gym.getPassHash());
  }

  @Test
  void registrationRequestToGym_shouldReturnNull() {
    RegistrationRequest request = null;

    Gym gym = mapper.registrationRequestToEntity(request);

    Assertions.assertNull(gym);
  }
}
