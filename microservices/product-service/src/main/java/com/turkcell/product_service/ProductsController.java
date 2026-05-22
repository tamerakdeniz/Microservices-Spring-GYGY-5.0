package com.turkcell.product_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/products")
@RestController
public class ProductsController {
    @GetMapping
    public String get() {
        System.out.println("ProductsController çalıştı");
        return "ProductsController";
    }
}
