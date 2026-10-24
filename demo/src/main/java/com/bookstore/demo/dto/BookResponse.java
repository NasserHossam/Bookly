package com.bookstore.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String category;
    private Double price;
    private String description;
    private String imageUrl;
}