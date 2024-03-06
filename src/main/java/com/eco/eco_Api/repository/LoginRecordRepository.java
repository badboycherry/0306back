package com.eco.eco_Api.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eco.eco_Api.entity.LoginRecord;
import com.eco.eco_Api.entity.UserEntity;

public interface LoginRecordRepository extends JpaRepository<LoginRecord, Long> {
    void deleteByUser(UserEntity user);
    @Query("SELECT COUNT(lr) > 0 FROM LoginRecord lr WHERE lr.user = :user AND DATE(lr.loginDate) = :loginDate")
    boolean existsByUserAndLoginDate(@Param("user") UserEntity user, @Param("loginDate") Date loginDate);
}
