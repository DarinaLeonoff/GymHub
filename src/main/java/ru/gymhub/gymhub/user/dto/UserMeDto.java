package ru.gymhub.gymhub.user.dto;

import lombok.Data;

@Data
public class UserMeDto {
    private String firstName;
    //   private String lastName;
//   private LocalDate birthday;
//   private String city;
//   private String phoneNumber;
    private String email;
    //    private LocalDateTime registrationDate;
    private boolean isActive;
}
