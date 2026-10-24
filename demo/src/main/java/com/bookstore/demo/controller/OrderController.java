package com.bookstore.demo.controller;

import com.bookstore.demo.dto.OrderResponse;
import com.bookstore.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(Authentication authentication) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.checkout(authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication) {
        return ResponseEntity.ok(orderService.getMyOrders(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            Authentication authentication,
            @PathVariable Long id) {

        return ResponseEntity.ok(orderService.getOrderById(authentication.getName(), id));
    }
}
