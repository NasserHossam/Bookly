package com.bookstore.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderItemResponse {

    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookImageUrl;
    private Double unitPrice;
    private Integer quantity;
    private Double lineTotal;
}
