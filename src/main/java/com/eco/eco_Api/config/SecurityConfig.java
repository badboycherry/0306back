package com.eco.eco_Api.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.eco.eco_Api.JWT.JWTFilter;
import com.eco.eco_Api.JWT.JWTUtil;
import com.eco.eco_Api.JWT.LoginFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final AuthenticationConfiguration authenticationConfiguration;
        // JWTUtil 주입
        private final JWTUtil jwtUtil;

        public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
                this.authenticationConfiguration = authenticationConfiguration;
                this.jwtUtil = jwtUtil;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
                return configuration.getAuthenticationManager();
        }

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
                return new BCryptPasswordEncoder();
        }
        
        @Bean
        public RestTemplate restTemplate() {
                return new RestTemplate();
        }
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .cors((corsCustomizer -> corsCustomizer
                                                .configurationSource(new CorsConfigurationSource() {

                                                        @Override
                                                        public CorsConfiguration getCorsConfiguration(
                                                                        HttpServletRequest request) {

                                                                CorsConfiguration configuration = new CorsConfiguration();

                                                                configuration.setAllowedOriginPatterns(
                                                                                Collections.singletonList("*"));

                                                                configuration.setAllowedMethods(
                                                                                Collections.singletonList("*"));
                                                                configuration.setAllowCredentials(true);
                                                                configuration.setAllowedHeaders(
                                                                                Collections.singletonList("*"));
                                                                configuration.setMaxAge(3600L);

                                                                configuration.setExposedHeaders(Collections
                                                                                .singletonList("Authorization"));

                                                                return configuration;
                                                        }
                                                })));

                http
                                .csrf((auth) -> auth.disable());

                http
                                .formLogin((auth) -> auth.disable());

                http
                                .httpBasic((auth) -> auth.disable());

                http
                                .authorizeHttpRequests((auth) -> auth
                                                .requestMatchers("/login", "/", "/join", "/check-username").permitAll()
                                                .requestMatchers(HttpMethod.PUT, "/users/{id}").authenticated()
                                                .requestMatchers(HttpMethod.PUT, "/api/user/update").authenticated()
                                                .requestMatchers(HttpMethod.GET, "/api/user/me").authenticated()
                                                .requestMatchers(HttpMethod.GET, "/quizzes/**").authenticated() // 퀴즈 관련
                                                .requestMatchers(HttpMethod.POST, "/quizzes/**").authenticated()
                                                .requestMatchers(HttpMethod.GET, "/api/quizzes").authenticated() // 인증
                                                                                                                 // 필요
                                                .requestMatchers(HttpMethod.POST, "/api/quizzes").authenticated()

                                                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                                                // 게시판 관련 경로 추가
                                                .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/posts/**").authenticated() // 게시글
                                                                                                                   // 작성은
                                                                                                                   // 인증된
                                                                                                                   // 사용자만
                                                                                                                   // 가능
                                                .requestMatchers(HttpMethod.PUT, "/api/posts/**").authenticated() // 게시글
                                                                                                                  // 수정은
                                                                                                                  // 인증된
                                                                                                                  // 사용자만
                                                                                                                  // 가능
                                                .requestMatchers(HttpMethod.DELETE, "/api/posts/**").authenticated() // 게시글
                                                                                                                     // 삭제는
                                                                                                                     // 인증된
                                                                                                                     // 사용자만
                                                                                                                     // 가능
                                                // 댓글 관련 경로 추가
                                                .requestMatchers(HttpMethod.GET, "/api/posts/*/comments/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/posts/*/comments/**")
                                                .authenticated() // 댓글 추가는 인증된 사용자만 가능
                                                .requestMatchers(HttpMethod.PUT, "/api/posts/*/comments/**")
                                                .authenticated() // 댓글 수정은 인증된 사용자만 가능
                                                .requestMatchers(HttpMethod.DELETE, "/api/posts/*/comments/**")
                                                .authenticated() // 댓글 삭제는 인증된 사용자만 가능
                                                .requestMatchers("/delete").authenticated() // 탈퇴는 인증된 사용자만 가능
                                                .requestMatchers(HttpMethod.GET, "/api/missions").authenticated()
                                                .requestMatchers(HttpMethod.POST, "/img_upload").authenticated()

                                                .requestMatchers(HttpMethod.GET, "/quizzes/**").authenticated() // 퀴즈 관련
                                                .requestMatchers(HttpMethod.POST, "/quizzes/attempt").authenticated()
                                                // 미션관련
                                                .requestMatchers(HttpMethod.GET, "/api/missions/random").authenticated()
                                                .requestMatchers(HttpMethod.POST, "/api/missions/complete")
                                                .authenticated()
                                                .requestMatchers(HttpMethod.POST, "/monthly/missions/complete")
                                                .authenticated()
                                                // 월별 활동
                                                .requestMatchers(HttpMethod.GET, "/monthly/missions/monthly-count")
                                                .authenticated()
                                                .requestMatchers(HttpMethod.GET, "/api/user-activities").authenticated()
                                                .anyRequest().authenticated());

                // JWTFilter 등록
                http
                                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

                // AuthenticationManager()와 JWTUtil 인수 전달
                http
                                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),
                                                jwtUtil),
                                                UsernamePasswordAuthenticationFilter.class);
                http
                                .sessionManagement((session) -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                return http.build();
        }
}