package com.eco.eco_Api.repository;

import java.util.List;

// QuizRepository.java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eco.eco_Api.entity.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
 
    @Query(value = "SELECT * FROM quiz ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Quiz> findRandomQuizzes();
}
