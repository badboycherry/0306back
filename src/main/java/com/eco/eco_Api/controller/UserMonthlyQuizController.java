package com.eco.eco_Api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eco.eco_Api.entity.UserMonthlyQuiz;
import com.eco.eco_Api.service.UserMonthlyQuizService;

@RestController
@RequestMapping("/api/quizzes")
public class UserMonthlyQuizController {

    @Autowired
    private UserMonthlyQuizService userMonthlyQuizService;

    @PostMapping("/complete")
    public ResponseEntity<?> completeQuiz(Authentication authentication) {
        // Authentication 객체를 사용하여 인증된 사용자의 사용자명(username)을 가져옵니다.
        String username = authentication.getName();
        userMonthlyQuizService.addQuizAttempt(username);
        return ResponseEntity.ok().body("Quiz attempt added successfully.");
    }

    @GetMapping("/monthly-count")
    public ResponseEntity<?> getMonthlyQuizCount(Authentication authentication,
            @RequestParam int year,
            @RequestParam int month) {
        String username = authentication.getName();
        List<UserMonthlyQuiz> monthlyQuizzes = userMonthlyQuizService.getUserMonthlyQuizCounts(username, year, month);
        if (monthlyQuizzes.isEmpty()) {
            return ResponseEntity.ok("No quiz attempts found for the specified month.");
        }
        return ResponseEntity.ok(monthlyQuizzes);
    }
}
