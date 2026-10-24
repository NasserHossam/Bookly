package com.bookstore.demo.service.impl;

import com.bookstore.demo.dto.ChangePasswordRequest;
import com.bookstore.demo.dto.UpdateProfileRequest;
import com.bookstore.demo.dto.UserRequest;
import com.bookstore.demo.dto.UserResponse;
import com.bookstore.demo.entity.Role;
import com.bookstore.demo.entity.User;
import com.bookstore.demo.exception.ConflictException;
import com.bookstore.demo.exception.ResourceNotFoundException;
import com.bookstore.demo.repository.UserRepository;
import com.bookstore.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse registerUser(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    @Override
    public UserResponse getCurrentUser(String email) {

        User user = findByEmailOrThrow(email);

        return toResponse(user);
    }

    @Override
    public UserResponse updateProfile(String email, UpdateProfileRequest request) {

        User user = findByEmailOrThrow(email);

        // Only check for a conflict if the email is actually changing —
        // otherwise a user saving their own unchanged email would always fail.
        if (!user.getEmail().equalsIgnoreCase(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    @Override
    public void changePassword(String email, ChangePasswordRequest request) {

        User user = findByEmailOrThrow(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private User findByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }
}
