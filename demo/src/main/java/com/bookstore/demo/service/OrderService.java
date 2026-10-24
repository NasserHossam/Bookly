package com.bookstore.demo.service;

import com.bookstore.demo.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse checkout(String email);

    List<OrderResponse> getMyOrders(String email);

    OrderResponse getOrderById(String email, Long orderId);
}
