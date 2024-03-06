package com.eco.eco_Api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eco.eco_Api.entity.QuizAttempt;
import com.eco.eco_Api.entity.UserEntity;
import com.eco.eco_Api.entity.UserMonthlyQuiz;
import com.eco.eco_Api.repository.QuizAttemptRepository;
import com.eco.eco_Api.repository.UserMonthlyQuizRepository;
import com.eco.eco_Api.repository.UserRepository;

@Service
public class QuizService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final UserRepository userRepository;
    private final UserMonthlyQuizRepository userMonthlyQuizRepository;

    @Autowired
    public QuizService(QuizAttemptRepository quizAttemptRepository, UserRepository userRepository,
            UserMonthlyQuizRepository userMonthlyQuizRepository) {
        this.quizAttemptRepository = quizAttemptRepository;
        this.userRepository = userRepository;
        this.userMonthlyQuizRepository = userMonthlyQuizRepository;
    }

    @Transactional
    public void saveQuizAttempt(String username) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        // 사용자 조회
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 퀴즈 시도 기록 저장
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUser(user);
        attempt.setQuizCount(1);
        attempt.setDate(now.toString());
        quizAttemptRepository.save(attempt);

        // UserMonthlyQuiz 기록 업데이트 또는 생성
        List<UserMonthlyQuiz> monthlyQuizzes = userMonthlyQuizRepository
                .findByUserAndYearAndMonth(user, year, month); // 반환 타입 변경

        UserMonthlyQuiz monthlyQuiz;
        if (monthlyQuizzes.isEmpty()) {
            monthlyQuiz = new UserMonthlyQuiz(user, year, month);
        } else {
            monthlyQuiz = monthlyQuizzes.get(0);
        }

        monthlyQuiz.setCount(monthlyQuiz.getCount() + 1);
        userMonthlyQuizRepository.save(monthlyQuiz);
    }

}

