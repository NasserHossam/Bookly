package com.bookstore.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {

    @NotNull(message = "bookId is required")
    private Long bookId;

    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be at least 1")
    private Integer quantity;
}
