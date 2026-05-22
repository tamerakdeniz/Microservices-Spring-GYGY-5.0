package com.turkcell.order_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/orders")
@RestController
public class OrdersController {
    @GetMapping
    public String get() {
        System.out.println("OrdersController çalıştı");
        return "OrdersController";
    }
}
