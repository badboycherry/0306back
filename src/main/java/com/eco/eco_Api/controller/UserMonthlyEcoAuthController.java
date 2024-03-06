package com.eco.eco_Api.controller;

import java.util.List;
import com.eco.eco_Api.entity.UserMonthlyEcoAuth;
import com.eco.eco_Api.service.UserMonthlyEcoAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import org.springframework.http.MediaType;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/eco-auth")
public class UserMonthlyEcoAuthController {

    private static final Logger LOGGER = Logger.getLogger(UserMonthlyEcoAuthController.class.getName());

    @Autowired
    private UserMonthlyEcoAuthService userMonthlyEcoAuthService;

    @Autowired
    private RestTemplate restTemplate;

    // 이미지 업로드 및 에코-인증 추가 엔드포인트
    @PostMapping("/img_upload")
    public ResponseEntity<String> handleImageUpload(
            @RequestParam("image") MultipartFile image, Authentication authentication) {

        // 사용자 이름 추출
        String username = authentication.getName();

        LOGGER.log(Level.INFO, "Received image for user: {0}", username);

        // 이미지 업로드 및 에코-인증 추가 처리
        // userMonthlyEcoAuthService.uploadImage(image, username);

        // Flask 서버로 이미지 전송
        boolean success = sendImageToFlask(image);

        // 성공 여부에 따라 count 증가
        // 성공 여부에 따라 JSON 응답 반환
        if (success) {
            userMonthlyEcoAuthService.increaseEcoAuthCount(username);
            return new ResponseEntity<>("{\"success\": true, \"message\": \"Count increased.\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{\"success\": false, \"message\": \"Count not increased.\"}", HttpStatus.OK);
        }
    }

    private boolean sendImageToFlask(MultipartFile image) {
        // Flask 서버의 이미지 업로드 엔드포인트 URL
        String flaskServerUrl = "http://localhost:5001/ocr"; // 실제 Flask 서버의 엔드포인트 URL로 변경

        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return "image.jpg"; // 파일 이름을 지정해주어야 합니다.
                }
            });
    
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(flaskServerUrl, requestEntity, String.class);
    
            LOGGER.log(Level.INFO, "Flask server response: {0}", responseEntity.getBody());
            // Flask 서버 응답이 "registration_success": true를 포함하는 경우에만 포인트 증가
            if (responseEntity.getBody().contains("\"registration_success\": true")) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error converting image to bytes.", e);
            return false;
        }
    }

    // 에코-인증 조회 엔드포인트
    @GetMapping("/monthly-count")
    public ResponseEntity<Integer> getUserMonthlyEcoCounts(
            @RequestParam String username,
            @RequestParam int year,
            @RequestParam int month) {
        List<UserMonthlyEcoAuth> ecoAuthList = userMonthlyEcoAuthService.getUserMonthlyEcoCounts(username, year, month);
        int ecoAuthCount = ecoAuthList.size(); // 에코-인증 횟수는 리스트의 크기로 표현

        LOGGER.log(Level.INFO, "Retrieved monthly eco count for user {0}: {1}", new Object[]{username, ecoAuthCount});

        return ResponseEntity.ok(ecoAuthCount);
    }
}
