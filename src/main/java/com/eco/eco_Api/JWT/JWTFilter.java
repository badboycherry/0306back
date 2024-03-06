package com.eco.eco_Api.JWT;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eco.eco_Api.DTO.CustomUserDetails;
import com.eco.eco_Api.entity.UserEntity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            //  로그인시에는 token null이 뜨는게 맞습니다 
            System.out.println("token null");
            filterChain.doFilter(request, response);

            // 조건이 해당되면 메소드 종료 (필수)
            return;
        }

        System.out.println("authorization now");
        // Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        // 토큰이 만료되었는지 확인
        if (jwtUtil.isExpired(token)) {
            // 토큰이 만료된 경우 새로운 토큰 발급
            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);
            String newToken = jwtUtil.createJwt(username, role);
            response.setHeader("Authorization", "Bearer " + newToken); // 새로운 토큰을 응답 헤더에 추가
            System.out.println("New token issued");
        }

        // 토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // userEntity를 생성하여 값 set
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword("temppassword");
        userEntity.setRole(role);

        // UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
                customUserDetails.getAuthorities());
        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);
        System.out.println("Authorization now: " + authorization); // 디버깅 문 추가
        filterChain.doFilter(request, response);
    }
}