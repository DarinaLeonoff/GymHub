package ru.gymhub.gymhub.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gymhub.gymhub.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
