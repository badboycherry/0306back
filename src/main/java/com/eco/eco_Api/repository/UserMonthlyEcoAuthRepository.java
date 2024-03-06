package com.eco.eco_Api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eco.eco_Api.entity.UserEntity;
import com.eco.eco_Api.entity.UserMonthlyEcoAuth;

public interface UserMonthlyEcoAuthRepository extends JpaRepository<UserMonthlyEcoAuth, Long >{
    void deleteByUser(UserEntity user);

    List<UserMonthlyEcoAuth> findByUserAndYearAndMonth(UserEntity user, int year, int month);

}
