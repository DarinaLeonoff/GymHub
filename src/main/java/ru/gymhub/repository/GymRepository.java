package ru.gymhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gymhub.entity.Gym;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {}
