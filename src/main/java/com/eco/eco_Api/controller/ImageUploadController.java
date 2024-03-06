package com.eco.eco_Api.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import com.eco.eco_Api.service.EcoImageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class ImageUploadController {

    private static final Logger logger = LoggerFactory.getLogger(ImageUploadController.class);
    private static final String FLASK_ENDPOINT = "http://localhost:5000";
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png", "image/jpg");

    @Autowired
    private EcoImageService ecoImageService;

    @PostMapping(value = "/img_upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAndFetchImages(@RequestParam("image") MultipartFile image) {
        if (image.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미지가 제공되지 않았습니다");
        }

        String contentType = image.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JPG, JPEG, PNG 이미지만 허용됩니다");
        }

        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            });

            WebClient webClient = WebClient.builder()
                    .baseUrl(FLASK_ENDPOINT)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                    .build();

            String flaskResponse = webClient.post()
                    .uri("/predict")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Flask 응답을 파싱하여 데이터베이스에서 해당 이미지 조회
            JsonNode rootNode = new ObjectMapper().readTree(flaskResponse);
            ArrayNode imagesArrayNode = new ObjectMapper().createArrayNode();

            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    JsonNode filenameNode = node.get("filename");
                    if (filenameNode != null) {
                        String decodedFilename = URLDecoder.decode(filenameNode.asText(), StandardCharsets.UTF_8.name());
                        byte[] imageData = ecoImageService.getImageByProductName(decodedFilename);
                        if (imageData != null) {
                            String base64Image = Base64.getEncoder().encodeToString(imageData);
                            ObjectNode imageNode = new ObjectMapper().createObjectNode();
                            imageNode.put("filename", decodedFilename);
                            imageNode.put("imageData", "data:image/jpeg;base64," + base64Image);
                            imagesArrayNode.add(imageNode);
                        }
                    }
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

            // 최종 결과 반환
            if (imagesArrayNode.size() > 0) {
                String jsonResponse = imagesArrayNode.toString();
                return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No images found.");
            }

        } catch (Exception e) {
            logger.error("이미지 처리 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 처리 중 오류가 발생했습니다: " + e.getMessage());
                        // ... 예외 처리 끝 ...
        }
    }
    // ... 클래스 끝 ...
}
