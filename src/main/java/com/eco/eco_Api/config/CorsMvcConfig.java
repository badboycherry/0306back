package com.eco.eco_Api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc // Spring MVC 활성화
public class CorsMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        // 모든 경로에 대해 CORS 설정 추가
        corsRegistry.addMapping("/**")
            // 허용할 오리진 설정
            .allowedOriginPatterns("*")
            // 허용할 HTTP 메서드 설정 (모든 메서드 허용)
            .allowedMethods("*")
            // 허용할 헤더 설정 (모든 헤더 허용)
            .allowedHeaders("*")
            // 인증정보 포함 여부 설정
            .allowCredentials(true)
            // 클라이언트에 노출할 헤더 설정
            .exposedHeaders("Authorization")
            // Preflight 요청의 캐시 지속 시간 설정 (초 단위)
            .maxAge(3600);
    }
}