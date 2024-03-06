package com.eco.eco_Api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eco.eco_Api.DTO.UserMonthlyActivityDTO;
import com.eco.eco_Api.service.UserActivityService;

@RestController
@RequestMapping("/api/user-activities")
public class UserActivityController {

    @Autowired
    private UserActivityService userActivityService;

    @GetMapping("/monthly")
    public ResponseEntity<UserMonthlyActivityDTO> getUserMonthlyActivity(
            Authentication authentication,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        
        // 인증된 사용자의 username 추출
        String username = authentication.getName();

        // 서비스 메소드 호출
        UserMonthlyActivityDTO activityDto = userActivityService.getUserMonthlyActivity(username, year, month);

        // 결과 반환
        return ResponseEntity.ok(activityDto);
    }
}