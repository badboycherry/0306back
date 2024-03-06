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

import com.eco.eco_Api.entity.Reply;
import com.eco.eco_Api.service.ReplyService;


@RestController
@RequestMapping("/api/posts/{postId}/comments/{commentId}/replies")
public class ReplyController {

    @Autowired
    ReplyService replyService;

    @PostMapping
    public Reply addReply(@PathVariable("commentId") Long commentId, @RequestBody Reply reply) {
        String username = getCurrentUsername();
        return replyService.addReply(commentId, reply.getContent(), username);
    }

    @GetMapping
    public ResponseEntity<List<Reply>> getReplies(@PathVariable("commentId") Long commentId) {
        List<Reply> replies = replyService.getRepliesByCommentId(commentId);
        return ResponseEntity.ok(replies);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<?> deleteReply(@PathVariable("commentId") Long commentId, @PathVariable("replyId") Long replyId) {
        String username = getCurrentUsername();
        replyService.deleteReply(replyId, username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{replyId}")
    public ResponseEntity<Reply> updateReply(@PathVariable("commentId") Long commentId, @PathVariable("replyId") Long replyId, @RequestBody Reply updatedReply) {
        String username = getCurrentUsername();
        Reply reply = replyService.updateReply(replyId, updatedReply.getContent(), username);
        return ResponseEntity.ok(reply);
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // 혹은 적절한 예외 처리
        }
        return authentication.getName();
    }
}
