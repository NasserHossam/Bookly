package com.bookstore.demo.service;

import com.bookstore.demo.dto.BookRequest;
import com.bookstore.demo.dto.BookResponse;

import java.util.List;

public interface BookService {

    List<BookResponse> getAllBooks();

    BookResponse getBookById(Long id);

    BookResponse createBook(BookRequest request);

    BookResponse updateBook(Long id, BookRequest request);

    void deleteBook(Long id);
}