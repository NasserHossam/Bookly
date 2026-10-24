package com.bookstore.demo.service;

import com.bookstore.demo.dto.CartItemRequest;
import com.bookstore.demo.dto.CartItemResponse;
import com.bookstore.demo.dto.UpdateCartItemRequest;

import java.util.List;

public interface CartService {

    List<CartItemResponse> getCart(String email);

    CartItemResponse addToCart(String email, CartItemRequest request);

    CartItemResponse updateQuantity(String email, Long cartItemId, UpdateCartItemRequest request);

    void removeItem(String email, Long cartItemId);

    void clearCart(String email);
}
