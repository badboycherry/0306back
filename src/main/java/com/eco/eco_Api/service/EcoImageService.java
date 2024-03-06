package com.eco.eco_Api.service;

import com.eco.eco_Api.entity.Product;
import com.eco.eco_Api.repository.EcoImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Service
public class EcoImageService {

    private static final Logger logger = LoggerFactory.getLogger(EcoImageService.class);

    private final EcoImageRepository ecoImageRepository;

    @Autowired
    public EcoImageService(EcoImageRepository ecoImageRepository) {
        this.ecoImageRepository = ecoImageRepository;
    }

    public byte[] getImageByProductName(String productName) {
        logger.info("Searching for image with productName: {}", productName);
        Optional<Product> product = ecoImageRepository.findByProductName(productName);
    
        if (product.isPresent()) {
            logger.info("Image found for productName: {}", productName);
            return product.get().getImage();
        } else {
            logger.info("No image found for productName: {}", productName);
            return null;
        }
    }
}