package com.eco.eco_Api.repository;

import com.eco.eco_Api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EcoImageRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String productName);
}