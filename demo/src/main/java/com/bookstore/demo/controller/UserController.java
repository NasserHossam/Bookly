package com.bookstore.demo.controller;

import com.bookstore.demo.dto.ChangePasswordRequest;
import com.bookstore.demo.dto.UpdateProfileRequest;
import com.bookstore.demo.dto.UserRequest;
import com.bookstore.demo.dto.UserResponse;
import com.bookstore.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody UserRequest request) {

        UserResponse response = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                userService.getCurrentUser(email)
        );
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                userService.updateProfile(email, request)
        );
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {

        String email = authentication.getName();

        userService.changePassword(email, request);

        return ResponseEntity.noContent().build();
    }
}
