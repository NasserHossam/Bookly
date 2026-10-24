package com.bookstore.demo.service;

import com.bookstore.demo.dto.LoginRequest;
import com.bookstore.demo.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}