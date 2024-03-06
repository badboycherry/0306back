package com.eco.eco_Api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eco.eco_Api.entity.Quiz;
import com.eco.eco_Api.entity.QuizAttempt;
import com.eco.eco_Api.entity.UserEntity;
import com.eco.eco_Api.repository.QuizAttemptRepository;
import com.eco.eco_Api.repository.QuizRepository;
import com.eco.eco_Api.repository.UserRepository;

@RestController
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/quizzes/random")
    public List<Quiz> getRandomQuizzes() {
        return quizRepository.findRandomQuizzes();
    }

    @PostMapping("/quizzes/attempt")
    public ResponseEntity<?> saveQuizAttempt(Authentication authentication) {
        String today = LocalDate.now().toString();
        UserEntity user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // 동일한 사용자의 동일 날짜 퀴즈 시도가 있는지 확인
        if (quizAttemptRepository.findByUserAndDate(user, today).isPresent()) {
            return ResponseEntity.badRequest().body("You have already attempted the quiz today.");
        }

        QuizAttempt attempt = new QuizAttempt();
        attempt.setUser(user); // 직접 UserEntity 객체를 설정
        attempt.setQuizCount(1);
        attempt.setDate(today);
        quizAttemptRepository.save(attempt);

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

}