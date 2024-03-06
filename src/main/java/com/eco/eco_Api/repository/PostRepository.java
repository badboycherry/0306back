package com.eco.eco_Api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eco.eco_Api.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    // 기본적인 CRUD 연산을 위한 메소드는 JpaRepository에 이미 정의되어 있습니다.
}