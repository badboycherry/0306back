package com.eco.eco_Api.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eco.eco_Api.entity.LoginRecord;
import com.eco.eco_Api.entity.UserEntity;
import com.eco.eco_Api.repository.LoginRecordRepository;
import com.eco.eco_Api.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginRecordRepository loginRecordRepository;

    @Transactional
    public void loginUser(String username) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            LocalDate today = LocalDate.now();

            // Check if there's a login record for today
            boolean existsLoginToday = loginRecordRepository.existsByUserAndLoginDate(user, java.sql.Date.valueOf(today));

            if (!existsLoginToday) {
                // If no login record for today, increment dailyCount and create a new login record
                user.setDailyCount(user.getDailyCount() + 1);
                userRepository.save(user);

                LoginRecord newRecord = new LoginRecord(user, java.sql.Date.valueOf(today));
                loginRecordRepository.save(newRecord);
            }
        }
    }
}
