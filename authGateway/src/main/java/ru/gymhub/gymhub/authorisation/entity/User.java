package ru.gymhub.gymhub.authorisation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDateTime created;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "acc_type", nullable = false)
    private AccountType accType;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private RoleType role;

    public enum AccountType {
        GYM, CLIENT
    }

    public enum RoleType {
        GYM_ADMIN, GYM_TRAINER, CLIENT
    }
}
