package com.bookstore.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String status;
    private List<OrderItemResponse> items;
}
