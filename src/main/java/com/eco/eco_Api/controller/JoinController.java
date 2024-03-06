package com.eco.eco_Api.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eco.eco_Api.DTO.JoinDTO;
import com.eco.eco_Api.entity.UserEntity;
import com.eco.eco_Api.repository.UserRepository;
import com.eco.eco_Api.service.JoinService;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;
    private final UserRepository userRepository;

    public JoinController(JoinService joinService, UserRepository userRepository) {
        this.joinService = joinService;
        this.userRepository = userRepository;
    }

    @PostMapping(value="/join", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> joinProcess(@RequestBody JoinDTO joinDTO) {
        boolean success = joinService.joinProcess(joinDTO);
        if (success) {
            return ResponseEntity.ok("가입이 완료되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("이미 존재하는 사용자 이름입니다: " + joinDTO.getUsername());
        }
    }
    
    // 로그인한 사용자의 정보를 조회하는 엔드포인트 추가
    @GetMapping("/api/user/me")
    public Optional<UserEntity> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return joinService.getUserByUsername(currentUsername);
    }

    @PutMapping("/users/{username}")
    public ResponseEntity<?> updateUser(@PathVariable("username") String username, @RequestBody JoinDTO joinDTO) {
        try {
            UserEntity updatedUser = joinService.updateUser(username, joinDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {
        try {
            boolean deleted = joinService.deleteUser(username);
            if (deleted) {
                return ResponseEntity.ok("사용자 계정이 성공적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("사용자 계정 삭제에 실패했습니다.");
        }
    }

    @GetMapping(value="/check-username", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> checkUsername(@RequestParam(name = "username") String username) {
        boolean exists = userRepository.existsByUsername(username);
        if (exists) {
            return ResponseEntity.badRequest().body("이미 존재하는 사용자 이름입니다: " + username);
        } else {
            return ResponseEntity.ok("사용 가능한 사용자 이름입니다.");
        }
    }

}