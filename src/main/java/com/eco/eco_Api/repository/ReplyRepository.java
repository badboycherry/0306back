package com.eco.eco_Api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eco.eco_Api.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByCommentId(Long commentId);
}
