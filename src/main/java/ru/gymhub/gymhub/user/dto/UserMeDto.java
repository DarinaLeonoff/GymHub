package ru.gymhub.gymhub.user.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserMeDto {
    private String firstName;
    private String lastName;
    private LocalDate birthDay;
    private String city;
    private String phone;
    private String email;
    private LocalDateTime created;
    private boolean isActive;
}
