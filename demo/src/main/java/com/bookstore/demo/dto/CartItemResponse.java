package com.bookstore.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CartItemResponse {

    private Long id;
    private BookResponse book;
    private Integer quantity;
    private Double subtotal;
}
