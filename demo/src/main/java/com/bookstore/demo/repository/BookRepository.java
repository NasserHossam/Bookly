package com.bookstore.demo.repository;

import com.bookstore.demo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}