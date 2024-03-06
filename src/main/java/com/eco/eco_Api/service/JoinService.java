package com.eco.eco_Api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eco.eco_Api.DTO.JoinDTO;
import com.eco.eco_Api.entity.UserEntity;
import com.eco.eco_Api.repository.LoginRecordRepository;
import com.eco.eco_Api.repository.MissionAttemptRepository;
import com.eco.eco_Api.repository.QuizAttemptRepository;
import com.eco.eco_Api.repository.UserMonthlyMissionRepository;
import com.eco.eco_Api.repository.UserMonthlyQuizRepository;
import com.eco.eco_Api.repository.UserRepository;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MissionAttemptRepository missionAttemptRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final LoginRecordRepository loginRecordRepository;
    private final UserMonthlyMissionRepository userMonthlyMissionRepository;
    private final UserMonthlyQuizRepository userMonthlyQuizRepository;

    @Autowired
    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                        MissionAttemptRepository missionAttemptRepository, QuizAttemptRepository quizAttemptRepository,
                        LoginRecordRepository loginRecordRepository, UserMonthlyMissionRepository userMonthlyMissionRepository,
                        UserMonthlyQuizRepository userMonthlyQuizRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.missionAttemptRepository = missionAttemptRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.loginRecordRepository = loginRecordRepository;
        this.userMonthlyMissionRepository = userMonthlyMissionRepository;
        this.userMonthlyQuizRepository = userMonthlyQuizRepository;
    }

    @Transactional
    public boolean joinProcess(JoinDTO joinDTO) {
        if (joinDTO.getPassword() == null) {
            // 비밀번호가 null일 경우 로그 출력 및 처리 중단
            System.out.println("비밀번호가 null입니다.");
            return false;
        }
        if (userRepository.existsByUsername(joinDTO.getUsername())) {
            // 이미 존재하는 사용자 아이디일 경우 로그 출력 및 처리 중단
            System.out.println("이미 존재하는 사용자 이름입니다: " + joinDTO.getUsername());
            return false;
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(joinDTO.getUsername());
        newUser.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        newUser.setName(joinDTO.getName());
        newUser.setAddress(joinDTO.getAddress());
        newUser.setRole("ROLE_USER"); // 기본 역할 설정

        userRepository.save(newUser);
        return true;
    }

    @Transactional
    public UserEntity updateUser(String username, JoinDTO joinDTO) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if (!user.getUsername().equals(currentUsername)) {
            throw new RuntimeException("You can only update your own information.");
        }

        // 클라이언트로부터 현재 비밀번호, 새 비밀번호, 새 비밀번호 확인을 받아와야 합니다.
        // JoinDTO에 이 정보를 추가해주세요.

        // 현재 비밀번호 확인은 클라이언트로부터 받아온 현재 비밀번호와 DB에 저장된 비밀번호를 비교하여 처리합니다.
        if (!bCryptPasswordEncoder.matches(joinDTO.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect.");
        }

        // 새 비밀번호와 새 비밀번호 확인이 일치하는지 확인합니다.
        if (!joinDTO.getNewPassword().equals(joinDTO.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match.");
        }

        // 새 비밀번호 설정
        if (joinDTO.getNewPassword() != null && !joinDTO.getNewPassword().isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(joinDTO.getNewPassword()));
        }

        // 이름, 주소 업데이트
        user.setName(joinDTO.getName());
        user.setAddress(joinDTO.getAddress());

        // 다른 필드들도 업데이트 가능

        return userRepository.save(user);
    }

    @Transactional
public boolean deleteUser(String username) {
    Optional<UserEntity> userOptional = userRepository.findByUsername(username);
    if (!userOptional.isPresent()) {
        return false; // 사용자를 찾지 못함
    }

    UserEntity user = userOptional.get();

    // 각 자식 엔티티의 레코드를 삭제
    missionAttemptRepository.deleteByUser(user);
    quizAttemptRepository.deleteByUser(user);
    loginRecordRepository.deleteByUser(user);
    userMonthlyMissionRepository.deleteByUser(user);
    userMonthlyQuizRepository.deleteByUser(user);

    // 마지막으로 UserEntity 레코드를 삭제
    userRepository.delete(user);
    return true;
}


    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}