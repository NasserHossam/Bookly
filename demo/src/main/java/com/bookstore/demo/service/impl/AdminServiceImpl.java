package com.bookstore.demo.service.impl;

import com.bookstore.demo.dto.AdminRequest;
import com.bookstore.demo.dto.AdminResponse;
import com.bookstore.demo.entity.Role;
import com.bookstore.demo.entity.User;
import com.bookstore.demo.repository.UserRepository;
import com.bookstore.demo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<AdminResponse> getAllAdmins() {

        return userRepository.findByRole(Role.ADMIN)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AdminResponse createAdmin(AdminRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User admin = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Role.ADMIN)
                .build();

        User savedAdmin = userRepository.save(admin);

        return mapToResponse(savedAdmin);
    }

    @Override
    public void deleteAdmin(Long id) {

        User admin = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Admin not found"));

        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("User is not an administrator");
        }

        userRepository.delete(admin);
    }

    private AdminResponse mapToResponse(User user) {

        return AdminResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }
}