package ru.gymhub.gymhub.authorisation.repository;

import org.springframework.data.repository.CrudRepository;
import ru.gymhub.gymhub.authorisation.entity.RefreshToken;

import java.util.Optional;

public interface RefreshRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUserId(Long userId);
}