package com.eco.eco_Api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eco.eco_Api.entity.QuizAttempt;
import com.eco.eco_Api.entity.UserEntity; // UserEntity를 임포트해야 합니다.

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Integer> {
    void deleteByUser(UserEntity user);
    Optional<QuizAttempt> findByUserAndDate(UserEntity user, String date);
}
