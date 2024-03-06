package com.eco.eco_Api.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinDTO {

    private String username; // 사용자 아이디
    private String password; // 비밀번호
    private String name; // 사용자 이름
    private String address; // 주소
    private String newPassword; // 새 비밀번호
    private String confirmPassword; // 비밀번호 확인
    private String currentPassword; // 현재 비밀번호
}
