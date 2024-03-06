package com.eco.eco_Api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eco.eco_Api.entity.UserEntity;
import com.eco.eco_Api.entity.UserMonthlyMission;
import com.eco.eco_Api.repository.UserMonthlyMissionRepository;
import com.eco.eco_Api.repository.UserRepository; // 필요한 경우 추가

@Service
public class UserMonthlyMissionService {

    @Autowired
    private UserMonthlyMissionRepository userMonthlyMissionRepository;

    @Autowired
    private UserRepository userRepository; // 필요한 경우 추가

    @Transactional
    public void updateMonthlyMissionRecord(UserEntity user, int year, int month, int completedMissionsCount) {
        // 유저, 연도, 월을 기준으로 기존 기록을 찾거나 새로운 기록을 생성합니다.
        UserMonthlyMission monthlyMission = userMonthlyMissionRepository
                .findByUserAndYearAndMonth(user, year, month)
                .orElseGet(() -> {
                    UserMonthlyMission newMonthlyMission = new UserMonthlyMission();
                    newMonthlyMission.setUser(user);
                    newMonthlyMission.setYear(year);
                    newMonthlyMission.setMonth(month);
                    newMonthlyMission.setCount(0); // 초기 미션 완료 횟수
                    newMonthlyMission.setCumulativeCount(0); // 초기 누적 미션 완료 횟수
                    return newMonthlyMission;
                });

        // 기존의 미션 완료 횟수에 새로운 완료 횟수를 더합니다.
        monthlyMission.setCount(monthlyMission.getCount() + completedMissionsCount);
        monthlyMission.setCumulativeCount(monthlyMission.getCumulativeCount() + completedMissionsCount);
        userMonthlyMissionRepository.save(monthlyMission);
    }

    public List<UserMonthlyMission> getUserMonthlyMissionCounts(String username, int year, int month) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found for username: " + username));
        return List.of(userMonthlyMissionRepository.findByUserAndYearAndMonth(user, year, month)
                .orElseThrow(() -> new IllegalArgumentException("No records found for the specified year and month")));
    }
}
