package com.eco.eco_Api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eco.eco_Api.entity.UserEntity;
import com.eco.eco_Api.entity.UserMonthlyMission;

public interface UserMonthlyMissionRepository extends JpaRepository<UserMonthlyMission, Long> {
    void deleteByUser(UserEntity user);
    Optional<UserMonthlyMission> findByUserAndYearAndMonth(UserEntity user, int year, int month);

    
}
