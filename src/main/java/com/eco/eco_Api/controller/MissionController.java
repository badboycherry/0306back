package com.eco.eco_Api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eco.eco_Api.entity.Mission;
import com.eco.eco_Api.service.MissionService;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionService missionService;

    @Autowired
    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completeMissions(@RequestBody List<Long> missionIds, Authentication authentication) {
        String username = authentication.getName(); // 로그인한 사용자의 username 획득
        missionService.completeMissions(missionIds, username); // 서비스에 미션 완료 요청
        return ResponseEntity.ok().build();
    }

    @GetMapping("/random")
    public ResponseEntity<List<Mission>> getRandomMissions() {
        List<Mission> randomMissions = missionService.getRandomMissions();
        return ResponseEntity.ok(randomMissions);
    }

}