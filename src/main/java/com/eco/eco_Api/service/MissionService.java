package com.eco.eco_Api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eco.eco_Api.entity.Mission;
import com.eco.eco_Api.entity.MissionAttempt;
import com.eco.eco_Api.entity.UserEntity;
import com.eco.eco_Api.repository.MissionAttemptRepository;
import com.eco.eco_Api.repository.MissionRepository;
import com.eco.eco_Api.repository.UserRepository;

@Service
public class MissionService {

    // 기존 필드 및 생성자는 유지, UserMonthlyMissionService 추가
    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final MissionAttemptRepository missionAttemptRepository;
    private final UserMonthlyMissionService userMonthlyMissionService;

    @Autowired
    public MissionService(MissionRepository missionRepository, UserRepository userRepository,
            MissionAttemptRepository missionAttemptRepository,
            UserMonthlyMissionService userMonthlyMissionService) { // 생성자에 추가
        this.missionRepository = missionRepository;
        this.userRepository = userRepository;
        this.missionAttemptRepository = missionAttemptRepository;
        this.userMonthlyMissionService = userMonthlyMissionService; // 초기화
    }

    @Transactional
    public void completeMissions(List<Long> missionIds, String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        LocalDate today = LocalDate.now();

        if (missionAttemptRepository.existsByUserAndAttemptDate(user, today)) {
            throw new RuntimeException("Mission attempt already exists for user " + username + " on date " + today);
        }

        // 미션 시도 저장
        MissionAttempt missionAttempt = new MissionAttempt();
        missionAttempt.setUser(user);
        missionAttempt.setAttemptDate(today);
        missionAttemptRepository.save(missionAttempt);
        // 미션을 완료한 사용자의 이름을 미션 엔티티의 username 필드에 설정
        for (Long missionId : missionIds) {
            Mission mission = missionRepository.findById(missionId)
                    .orElseThrow(() -> new RuntimeException("Mission not found with ID: " + missionId));
            mission.setUsername(username);
            missionRepository.save(mission);
        }

        
    }

    public List<Mission> getRandomMissions() {
        // 임시로 미션 리스트를 반환하는 로직; 실제 구현은 데이터베이스에서 무작위 미션을 가져오는 방식이 될 수 있음
        return missionRepository.findRandomMissions(); // 가정: MissionRepository에 이 메서드가 정의되어 있음
    }
}
