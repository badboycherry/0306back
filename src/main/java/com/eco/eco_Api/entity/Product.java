package com.eco.eco_Api.entity;

import java.util.Base64;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "eco_image")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private String mark_1;

    @Column(nullable = false)
    private String mark_2;

    @Lob
    @Column(nullable = false)
    private byte[] image;

    // 이미지 데이터를 base64 문자열로 설정하는 메서드
    public void setImageBase64(String imageBase64) {
        this.image = Base64.getDecoder().decode(imageBase64);
    }
}