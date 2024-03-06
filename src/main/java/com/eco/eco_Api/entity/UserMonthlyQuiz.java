package com.eco.eco_Api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "user_monthly_quiz")

public class UserMonthlyQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private int year;
    private int month;
    private int count;
    private int cumulativeCount;

    public UserMonthlyQuiz() {
        // 기본 생성자
    }

    public UserMonthlyQuiz(UserEntity user, int year, int month) {
        this.user = user;
        this.year = year;
        this.month = month;
        this.count = 0; // 초기값 설정
        this.cumulativeCount = 0; // 초기값 설정
    }

    // Getter, Setter
}
