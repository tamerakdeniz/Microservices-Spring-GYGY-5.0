package com.turkcell.product_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.turkcell.product_service.entity.Product;
import com.turkcell.product_service.event.TestEvent;
import com.turkcell.product_service.repository.ProductRepository;

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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> create(@RequestBody CreateProductRequest request) {
        validate(request);

        var product = productRepository.save(new Product(request.name().trim(), request.stockQuantity()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ProductResponse(product.getId(), product.getName(), product.getStockQuantity()));
    }

    @PostMapping(params = "message")
    public String test(@RequestParam String message) {
        var event = new TestEvent(message, UUID.randomUUID());
        streamBridge.send("testEvent-out-0", event);
        return "Başarılı";
    }

    private void validate(CreateProductRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product request is required");
        }
        if (request.id() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product id must not be provided");
        }
        if (request.name() == null || request.name().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is required");
        }
        if (request.stockQuantity() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock quantity must be zero or greater");
        }
    }

    public record CreateProductRequest(UUID id, String name, int stockQuantity) {
    }

    public record ProductResponse(UUID id, String name, int stockQuantity) {
    }
}
