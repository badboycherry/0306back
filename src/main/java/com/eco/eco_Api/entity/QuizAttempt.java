package com.eco.eco_Api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private int quizCount;
    private String date; // 날짜

    @ManyToOne
    @JoinColumn(name = "user_id") // UserEntity와의 관계를 나타내는 필드
    @JsonBackReference
    private UserEntity user; // 사용자 엔티티에 대한 참조

    // 생성자, 게터, 세터 등 필요한 부분을 추가할 수 있습니다.
}