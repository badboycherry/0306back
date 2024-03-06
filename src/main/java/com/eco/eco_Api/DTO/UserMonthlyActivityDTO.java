package com.eco.eco_Api.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 자동 생성
public class UserMonthlyActivityDTO {
    private String username;
    private int year;
    private int month;
    private int ecoCount; // 에코인증
    private int missionCount;
    private int quizCount;
}