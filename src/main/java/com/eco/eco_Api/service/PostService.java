package com.eco.eco_Api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eco.eco_Api.entity.PostEntity;
import com.eco.eco_Api.repository.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // 게시글 생성
    public PostEntity createPost(PostEntity post) {
        return postRepository.save(post);
    }

    // 게시글 조회 (단일 게시글)
    public Optional<PostEntity> getPostById(Long id) {
        return postRepository.findById(id);
    }

    // 게시글 전체 조회
    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

    // 게시글 수정
    @Transactional
    public PostEntity updatePost(Long id, PostEntity postDetails) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + id));

        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        // 필요하다면 여기에서 추가적인 필드를 업데이트 할 수 있습니다.

        return postRepository.save(post); // 수정된 게시글 저장
    }

    // 게시글 삭제
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}