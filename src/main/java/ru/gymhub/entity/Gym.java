package ru.gymhub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gym")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String address;

    private String tel;
    private String email;
}
