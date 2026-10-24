package com.bookstore.demo.controller;

import com.bookstore.demo.dto.CartItemRequest;
import com.bookstore.demo.dto.CartItemResponse;
import com.bookstore.demo.dto.UpdateCartItemRequest;
import com.bookstore.demo.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(Authentication authentication) {
        return ResponseEntity.ok(cartService.getCart(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<CartItemResponse> addToCart(
            Authentication authentication,
            @Valid @RequestBody CartItemRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartService.addToCart(authentication.getName(), request));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<CartItemResponse> updateQuantity(
            Authentication authentication,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request) {

        return ResponseEntity.ok(
                cartService.updateQuantity(authentication.getName(), itemId, request)
        );
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeItem(
            Authentication authentication,
            @PathVariable Long itemId) {

        cartService.removeItem(authentication.getName(), itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
