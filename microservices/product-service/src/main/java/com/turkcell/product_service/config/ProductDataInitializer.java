package com.turkcell.product_service.config;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.turkcell.product_service.entity.Product;
import com.turkcell.product_service.repository.ProductRepository;

@Configuration
public class ProductDataInitializer {

    @Bean
    CommandLineRunner seedProducts(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() > 0) {
                return;
            }

            productRepository.saveAll(List.of(
                    new Product(UUID.fromString("11111111-1111-1111-1111-111111111111"), "Phone", 50),
                    new Product(UUID.fromString("22222222-2222-2222-2222-222222222222"), "Tablet", 30),
                    new Product(UUID.fromString("33333333-3333-3333-3333-333333333333"), "Headset", 100)));
        };
    }
}
