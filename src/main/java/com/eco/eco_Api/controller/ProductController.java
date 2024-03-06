package com.eco.eco_Api.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eco.eco_Api.entity.Product;
import com.eco.eco_Api.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // 이미지 데이터를 base64로 인코딩하는 함수
    private String encodeImage(byte[] image) {
        return Base64.getEncoder().encodeToString(image);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            // 이미지 데이터를 base64로 인코딩하여 설정
            product.setImageBase64(encodeImage(product.getImage()));
            product.setProductName(new String(product.getProductName().getBytes(), StandardCharsets.UTF_8));
            return ResponseEntity.ok().body(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (!products.isEmpty()) {
            // 모든 제품의 이미지 데이터를 base64로 인코딩하여 설정
            for (Product product : products) {
                product.setImageBase64(encodeImage(product.getImage()));
            }
            return ResponseEntity.ok().body(products);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable("category") String category) {
        List<Product> products = productRepository.findByCategory(category);
        if (!products.isEmpty()) {
            // 해당 카테고리의 모든 제품의 이미지 데이터를 base64로 인코딩하여 설정
            for (Product product : products) {
                product.setImageBase64(encodeImage(product.getImage()));
            }
            return ResponseEntity.ok().body(products);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}