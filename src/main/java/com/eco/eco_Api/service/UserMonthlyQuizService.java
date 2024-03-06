package com.eco.eco_Api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eco.eco_Api.entity.UserEntity;
import com.eco.eco_Api.entity.UserMonthlyQuiz;
import com.eco.eco_Api.repository.UserMonthlyQuizRepository;
import com.eco.eco_Api.repository.UserRepository;

@Service
public class UserMonthlyQuizService {

    @Autowired
    private UserMonthlyQuizRepository userMonthlyQuizRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void addQuizAttempt(String username) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        UserMonthlyQuiz userMonthlyQuiz = getUserMonthlyQuiz(user, year, month);

        // 새로운 퀴즈 시도를 기록하고, 누적 카운트를 업데이트합니다.
        userMonthlyQuiz.setCount(userMonthlyQuiz.getCount() + 1);
        // 누적 카운트 업데이트 로직 추가
        userMonthlyQuiz.setCumulativeCount(userMonthlyQuiz.getCumulativeCount() + 1);
        userMonthlyQuizRepository.save(userMonthlyQuiz);
    }

    private UserMonthlyQuiz getUserMonthlyQuiz(UserEntity user, int year, int month) {
        List<UserMonthlyQuiz> monthlyQuizzes = userMonthlyQuizRepository.findByUserAndYearAndMonth(user, year, month);
        if (monthlyQuizzes.isEmpty()) {
            UserMonthlyQuiz newUserMonthlyQuiz = new UserMonthlyQuiz();
            newUserMonthlyQuiz.setUser(user);
            newUserMonthlyQuiz.setYear(year);
            newUserMonthlyQuiz.setMonth(month);
            newUserMonthlyQuiz.setCount(0); // 초기 퀴즈 시도 횟수 설정
            newUserMonthlyQuiz.setCumulativeCount(0); // 누적 카운트 초기화
            return userMonthlyQuizRepository.save(newUserMonthlyQuiz);
        } else {
            return monthlyQuizzes.get(0);
        }
    }

    public List<UserMonthlyQuiz> getUserMonthlyQuizCounts(String username, int year, int month) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    
        return userMonthlyQuizRepository.findByUserAndYearAndMonth(user, year, month);
    }
    
}
