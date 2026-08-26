package ru.gymhub.gymhub.authorisation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gymhub.gymhub.authorisation.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
