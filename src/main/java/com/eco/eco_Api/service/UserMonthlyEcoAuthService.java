package com.eco.eco_Api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.eco.eco_Api.entity.UserEntity;
import com.eco.eco_Api.entity.UserMonthlyEcoAuth;

import com.eco.eco_Api.repository.UserMonthlyEcoAuthRepository;
import com.eco.eco_Api.repository.UserRepository;

@Service
public class UserMonthlyEcoAuthService {

    @Autowired
    private UserMonthlyEcoAuthRepository userMonthlyEcoAuthRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void increaseEcoAuthCount(String username) {
        // 사용자 정보 조회
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // 현재 년도와 월 가져오기
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        // 해당 사용자, 년도, 월에 대한 레코드 가져오기
        UserMonthlyEcoAuth userMonthlyEcoAuth = getUserMonthlyEcoAuth(user, year, month);

        // 에코인증 누적 카운트 증가
        userMonthlyEcoAuth.setCumulativeCount(userMonthlyEcoAuth.getCumulativeCount() + 100);
        userMonthlyEcoAuthRepository.save(userMonthlyEcoAuth);
    }

    private UserMonthlyEcoAuth getUserMonthlyEcoAuth(UserEntity user, int year, int month) {
        List<UserMonthlyEcoAuth> monthlyEcoAuthList = userMonthlyEcoAuthRepository.findByUserAndYearAndMonth(user, year, month);
        
        if (!monthlyEcoAuthList.isEmpty()) {
            // 레코드가 이미 존재하면 반환합니다.
            return monthlyEcoAuthList.get(0);
        } else {
            // 레코드가 존재하지 않으면 새로운 레코드를 생성합니다.
            UserMonthlyEcoAuth newUserMonthlyEcoAuth = new UserMonthlyEcoAuth();
            newUserMonthlyEcoAuth.setUser(user);
            newUserMonthlyEcoAuth.setYear(year);
            newUserMonthlyEcoAuth.setMonth(month);
            newUserMonthlyEcoAuth.setCumulativeCount(0); // 누적 카운트 초기화
            return userMonthlyEcoAuthRepository.save(newUserMonthlyEcoAuth);
        }
    }

    public List<UserMonthlyEcoAuth> getUserMonthlyEcoCounts(String username, int year, int month) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    
        return userMonthlyEcoAuthRepository.findByUserAndYearAndMonth(user, year, month);
    }
}