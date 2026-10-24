package com.bookstore.demo.dto;

import com.bookstore.demo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;
}