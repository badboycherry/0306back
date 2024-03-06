package com.eco.eco_Api.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eco.eco_Api.entity.MissionAttempt;
import com.eco.eco_Api.entity.UserEntity;

public interface MissionAttemptRepository extends JpaRepository<MissionAttempt, Long> {
    void deleteByUser(UserEntity user);
    Optional<MissionAttempt> findByUserAndAttemptDate(UserEntity user, LocalDate attemptDate);
    boolean existsByUserAndAttemptDate(UserEntity user, LocalDate attemptDate);

}
