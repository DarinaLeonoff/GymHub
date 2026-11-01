package ru.gymhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.gymhub.annotation.TelNumber;

@Builder
@Data
public class UpdateGymRequest {
  @Size(min = 1, max = 50)
  private String name;

  @Size(max = 500)
  private String description;

  @Size(min = 1, max = 500)
  private String address;

  @TelNumber private String tel;

  @Email private String email;
}
