package com.eco.eco_Api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eco.eco_Api.entity.UserEntity;
import com.eco.eco_Api.entity.UserMonthlyMission;
import com.eco.eco_Api.repository.UserRepository;
import com.eco.eco_Api.service.UserMonthlyMissionService;

@RestController
@RequestMapping("/monthly/missions")
public class UserMonthlyMissionController {

    private final UserMonthlyMissionService userMonthlyMissionService;
    private final UserRepository userRepository; // UserRepository 추가

    @Autowired
    public UserMonthlyMissionController(UserMonthlyMissionService userMonthlyMissionService,
            UserRepository userRepository) {
        this.userMonthlyMissionService = userMonthlyMissionService;
        this.userRepository = userRepository;
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completeMission(Authentication authentication, @RequestBody List<Long> missionIds) {
        String username = authentication.getName();
        LocalDate today = LocalDate.now();

        // userRepository를 사용하여 UserEntity를 가져옵니다.
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // UserEntity 객체와 함께 updateMonthlyMissionRecord 메서드를 한 번만 호출합니다.
        userMonthlyMissionService.updateMonthlyMissionRecord(user, today.getYear(), today.getMonthValue(),
                1); // 미션 완료 횟수를 한 번만 증가시킵니다.
        return ResponseEntity.ok().body("Mission(s) completed successfully.");
    }

    @GetMapping("/monthly-count")
    public ResponseEntity<List<UserMonthlyMission>> getMonthlyMissionCount(Authentication authentication,
            @RequestParam int year,
            @RequestParam int month) {
        String username = authentication.getName();
        List<UserMonthlyMission> monthlyMissions = userMonthlyMissionService.getUserMonthlyMissionCounts(username, year,
                month);
        return ResponseEntity.ok(monthlyMissions);
    }
}