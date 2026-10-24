package com.bookstore.demo.controller;

import com.bookstore.demo.dto.AdminRequest;
import com.bookstore.demo.dto.AdminResponse;
import com.bookstore.demo.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @PostMapping
    public ResponseEntity<AdminResponse> createAdmin(
            @Valid @RequestBody AdminRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminService.createAdmin(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {

        adminService.deleteAdmin(id);

        return ResponseEntity.noContent().build();
    }
}