package ru.gymhub.gymhub.authorisation.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gymhub.gymhub.authorisation.entity.User;
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

    @ValidPassword
    private String password;

    @NotNull
    private User.AccountType accType;

    @NotNull
    private User.RoleType role;
}
