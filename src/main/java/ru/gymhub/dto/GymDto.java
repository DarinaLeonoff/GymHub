package ru.gymhub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GymDto {
    private Long id;

    private String name;
    private String description;
    private String address;

    private String tel;
    private String email;
}
