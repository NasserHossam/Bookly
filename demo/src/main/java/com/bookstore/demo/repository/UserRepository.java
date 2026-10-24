package com.bookstore.demo.repository;

import com.bookstore.demo.entity.Role;
import com.bookstore.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);
}