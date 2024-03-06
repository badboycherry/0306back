package com.eco.eco_Api.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LoginRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Temporal(TemporalType.DATE)
    private Date loginDate;

    public LoginRecord() {
    }

    // UserEntity와 java.util.Date를 인자로 받는 생성자 추가
    public LoginRecord(UserEntity user, Date loginDate) {
        this.user = user;
        this.loginDate = loginDate;
    }
}
