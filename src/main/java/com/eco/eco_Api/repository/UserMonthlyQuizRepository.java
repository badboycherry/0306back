package com.eco.eco_Api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eco.eco_Api.entity.UserEntity;
import com.eco.eco_Api.entity.UserMonthlyQuiz;

public interface UserMonthlyQuizRepository extends JpaRepository<UserMonthlyQuiz, Long> {
    void deleteByUser(UserEntity user);
    List<UserMonthlyQuiz> findByUserAndYearAndMonth(UserEntity user, int year, int month);

    List<UserMonthlyQuiz> findByUser_UsernameAndYearAndMonth(String username, int year, int month);

}
