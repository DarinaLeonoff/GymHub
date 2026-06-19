package ru.gymhub.gymhub.authorisation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gymhub.gymhub.authorisation.validation.ValidPassword;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RegisterRequest {
    @Email
    @Size(max = 255)
    @NotBlank
    private String email;

    @Size(min=1, max=50)
    @NotBlank
    private String firstName;

    @ValidPassword
    private String password;
}
