package com.eco.eco_Api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eco.eco_Api.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
}