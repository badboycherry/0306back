package com.eco.eco_Api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eco.eco_Api.entity.Comment;
import com.eco.eco_Api.service.CommentService;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Comment addComment(@PathVariable("postId") Long postId, @RequestBody Comment comment) {
        String username = getCurrentUsername(); // 현재 로그인한 사용자의 이름을 가져옵니다.
        return commentService.addComment(postId, comment.getContent(), username);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId) {
        String username = getCurrentUsername();
        commentService.deleteComment(commentId, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable("postId") Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestBody Comment updatedComment) {
        String username = getCurrentUsername();
        Comment result = commentService.updateComment(commentId, updatedComment.getContent(), username);
        return ResponseEntity.ok(result);
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return authentication.getName();
    }
}
