package com.eco.eco_Api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.eco_Api.entity.Comment;
import com.eco.eco_Api.entity.Reply;
import com.eco.eco_Api.repository.CommentRepository;
import com.eco.eco_Api.repository.ReplyRepository;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private CommentRepository commentRepository;

    public Reply addReply(Long commentId, String content, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));

        Reply reply = new Reply();
        reply.setComment(comment);
        reply.setContent(content);
        reply.setUsername(username);
        return replyRepository.save(reply);
    }

    public List<Reply> getRepliesByCommentId(Long commentId) {
        return replyRepository.findByCommentId(commentId);
    }

    public void deleteReply(Long replyId, String username) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("Reply not found"));
        if (!reply.getUsername().equals(username)) {
            throw new RuntimeException("You can only delete your own replies");
        }
        replyRepository.delete(reply);
    }

    public Reply updateReply(Long replyId, String newContent, String username) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("Reply not found with id " + replyId));
        if (!reply.getUsername().equals(username)) {
            throw new RuntimeException("You can only update your own replies");
        }
        reply.setContent(newContent);
        return replyRepository.save(reply);
    }
}
