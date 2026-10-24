package com.bookstore.demo.service;

import com.bookstore.demo.dto.ChangePasswordRequest;
import com.bookstore.demo.dto.UpdateProfileRequest;
import com.bookstore.demo.dto.UserRequest;
import com.bookstore.demo.dto.UserResponse;

public interface UserService {

    UserResponse registerUser(UserRequest request);

    UserResponse getCurrentUser(String email);

    UserResponse updateProfile(String email, UpdateProfileRequest request);

    void changePassword(String email, ChangePasswordRequest request);
}
