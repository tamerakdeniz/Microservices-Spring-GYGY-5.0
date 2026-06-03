package com.turkcell.product_service.controller;

import java.util.UUID;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.product_service.event.TestEvent;
import com.turkcell.product_service.repository.ProductRepository;

import java.util.List;

@RequestMapping("/api/products")
@RestController
public class ProductsController {
    private final StreamBridge streamBridge;
    private final ProductRepository productRepository;

    public ProductsController(StreamBridge streamBridge, ProductRepository productRepository) {
        this.streamBridge = streamBridge;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getStockQuantity()))
                .toList();
    }

    @PostMapping
    public String test(@RequestParam String message) {
        var event = new TestEvent(message, UUID.randomUUID());
        streamBridge.send("testEvent-out-0", event);
        return "Başarılı";
    }

    public record ProductResponse(UUID id, String name, int stockQuantity) {
    }
}
