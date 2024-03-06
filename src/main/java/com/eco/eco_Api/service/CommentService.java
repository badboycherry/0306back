package com.eco.eco_Api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.eco_Api.entity.Comment;
import com.eco.eco_Api.entity.PostEntity;
import com.eco.eco_Api.repository.CommentRepository;
import com.eco.eco_Api.repository.PostRepository;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository; // PostRepository 주입

    public Comment addComment(Long postId, String content, String username) {
        Comment comment = new Comment();

        // PostEntity 찾기
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + postId));

        comment.setPost(post); // 찾은 PostEntity를 설정
        comment.setContent(content);
        comment.setUsername(username);
        return commentRepository.save(comment);
    }
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getUsername().equals(username)) {
            throw new RuntimeException("You can only delete your own comments");
        }
        commentRepository.delete(comment);
    }

    // 댓글 수정
    public Comment updateComment(Long commentId, String newContent, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id " + commentId));
        
        // 사용자가 댓글의 작성자인지 확인
        if (!comment.getUsername().equals(username)) {
            throw new RuntimeException("You can only update your own comments");
        }
        
        comment.setContent(newContent); // 댓글 내용 업데이트
        return commentRepository.save(comment); // 업데이트된 댓글 저장
    }
    
}