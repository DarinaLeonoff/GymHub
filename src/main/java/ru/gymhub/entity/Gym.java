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

  @Column(name = "name", nullable = false)
  private String name;
  @Column(name = "description", nullable = false)
  private String description;
  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "tel", nullable = false)
  private String tel;

  @Column(name = "email", nullable = false)
  private String email;
  @Column(name = "pass_hash", nullable = false, length = 255)
  private String passHash;
}
