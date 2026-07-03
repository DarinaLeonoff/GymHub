package ru.gymhub.gymhub.authorisation.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gymhub.gymhub.authorisation.validation.ValidPassword;

import java.time.LocalDate;

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

    @Size(min=1, max=50)
    private String lastName;

    @ValidPassword
    private String password;

    @NotNull
    @Past
    private LocalDate birthDay;

    @Size(min=1, max=100)
    @NotBlank
    private String city;

    @Size(min = 11, max = 12)
    @NotBlank
    private String phone;
}
