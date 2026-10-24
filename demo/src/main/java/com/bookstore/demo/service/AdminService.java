package com.bookstore.demo.service;

import com.bookstore.demo.dto.AdminRequest;
import com.bookstore.demo.dto.AdminResponse;

import java.util.List;

public interface AdminService {

    List<AdminResponse> getAllAdmins();

    AdminResponse createAdmin(AdminRequest request);

    void deleteAdmin(Long id);
}