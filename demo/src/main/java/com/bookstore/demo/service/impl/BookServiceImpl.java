package com.bookstore.demo.service.impl;

import com.bookstore.demo.dto.BookRequest;
import com.bookstore.demo.dto.BookResponse;
import com.bookstore.demo.entity.Book;
import com.bookstore.demo.exception.ResourceNotFoundException;
import com.bookstore.demo.repository.BookRepository;
import com.bookstore.demo.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BookResponse getBookById(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Book not found"));

        return mapToResponse(book);
    }

    @Override
    public BookResponse createBook(BookRequest request) {

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .category(request.getCategory())
                .price(request.getPrice())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();

        Book savedBook = bookRepository.save(book);

        return mapToResponse(savedBook);
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest request) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Book not found"));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setCategory(request.getCategory());
        book.setPrice(request.getPrice());
        book.setDescription(request.getDescription());
        book.setImageUrl(request.getImageUrl());

        Book updatedBook = bookRepository.save(book);

        return mapToResponse(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Book not found"));

        bookRepository.delete(book);
    }

    private BookResponse mapToResponse(Book book) {

        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .category(book.getCategory())
                .price(book.getPrice())
                .description(book.getDescription())
                .imageUrl(book.getImageUrl())
                .build();
    }
}
