package com.bookstore.demo.dto;

import com.bookstore.demo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String email;
    private String name;
    private Role role;
}