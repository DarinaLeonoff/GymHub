package ru.gymhub.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gymhub.annotation.TelNumber;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGymRequest {
  @NotBlank
  @Size(min = 1, max = 50)
  private String name;

  @Size(max = 500)
  private String description;

  @NotBlank
  @Size(min = 1, max = 500)
  private String address;

  @TelNumber @NotBlank private String tel;

  @Email @NotBlank private String email;

  private String passHash = "password";
}
