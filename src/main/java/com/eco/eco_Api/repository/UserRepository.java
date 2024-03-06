package com.eco.eco_Api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eco.eco_Api.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Boolean existsByUsername(String username);

    // username을 받아 DB 테이블에서 회원을 조회하는 메소드 작성
    Optional<UserEntity> findByUsername(String username);
}



