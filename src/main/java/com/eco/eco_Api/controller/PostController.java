package com.eco.eco_Api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.eco.eco_Api.entity.PostEntity;
import com.eco.eco_Api.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // 인증되지 않은 경우
        }
        return authentication.getName();
    }

    // 게시글 작성
    @PostMapping
    public PostEntity createPost(@RequestBody PostEntity post) {
        String username = getCurrentUsername();
        post.setUsername(username);
        return postService.createPost(post);
    }

    // 모든 게시글 조회
    @GetMapping
    public List<PostEntity> getAllPosts() {
        return postService.getAllPosts();
    }

    // 특정 게시글 조회
    @GetMapping("/{id}")
    public PostEntity getPostById(@PathVariable("id") Long id) {
        return postService.getPostById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + id));
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public PostEntity updatePost(@PathVariable("id") Long id, @RequestBody PostEntity postDetails) {
        String username = getCurrentUsername();
        PostEntity existingPost = postService.getPostById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + id));

        // 현재 사용자가 게시글의 작성자인지 확인
        if (!existingPost.getUsername().equals(username)) {
            throw new RuntimeException("You are not the owner of this post");
        }

        return postService.updatePost(id, postDetails);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        String username = getCurrentUsername();
        PostEntity existingPost = postService.getPostById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + id));

        // 현재 사용자가 게시글의 작성자인지 확인
        if (!existingPost.getUsername().equals(username)) {
            throw new RuntimeException("You are not the owner of this post");
        }

        postService.deletePost(id);
        return "Post with id " + id + " has been deleted successfully";
    }
}