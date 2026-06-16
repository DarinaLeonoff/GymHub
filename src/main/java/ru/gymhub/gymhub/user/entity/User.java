package ru.gymhub.gymhub.user.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.gymhub.gymhub.authorisation.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String firstName;
//   private String lastName;
//   private LocalDate birthday;
//   private String city;
//   private String phoneNumber;
    private String email;
//    private LocalDateTime registrationDate;
    private String passwordHash;
    private boolean isActive;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name="user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;
}
