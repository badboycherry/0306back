package com.eco.eco_Api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.eco_Api.DTO.UserMonthlyActivityDTO;
import com.eco.eco_Api.entity.UserEntity;
import com.eco.eco_Api.entity.UserMonthlyMission;
import com.eco.eco_Api.entity.UserMonthlyQuiz;
import com.eco.eco_Api.repository.UserMonthlyMissionRepository;
import com.eco.eco_Api.repository.UserMonthlyQuizRepository;
import com.eco.eco_Api.repository.UserRepository;
import com.eco.eco_Api.entity.UserMonthlyEcoAuth;
import com.eco.eco_Api.repository.UserMonthlyEcoAuthRepository;

@Service
public class UserActivityService {
    @Autowired
    private UserMonthlyEcoAuthRepository userMonthlyEcoAuthRepository;

    @Autowired
    private UserMonthlyMissionRepository userMonthlyMissionRepository;

    @Autowired
    private UserMonthlyQuizRepository userMonthlyQuizRepository;

    @Autowired
    private UserRepository userRepository;

    public UserMonthlyActivityDTO getUserMonthlyActivity(String username, int year, int month) {
        // 사용자 정보 조회
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        // Eco 인증 조회
        int ecoCount = userMonthlyEcoAuthRepository.findByUserAndYearAndMonth(user, year, month)
                .stream().mapToInt(UserMonthlyEcoAuth::getCumulativeCount).sum();

        // 미션 카운트 조회
        int missionCount = userMonthlyMissionRepository.findByUserAndYearAndMonth(user, year, month)
                .map(UserMonthlyMission::getCount).orElse(0);

        // 퀴즈 카운트 조회
        int quizCount = userMonthlyQuizRepository.findByUserAndYearAndMonth(user, year, month)
                .stream().mapToInt(UserMonthlyQuiz::getCount).sum();

        // DTO 생성 및 반환
        return new  UserMonthlyActivityDTO(username, year, month, ecoCount, missionCount, quizCount);
    }
}