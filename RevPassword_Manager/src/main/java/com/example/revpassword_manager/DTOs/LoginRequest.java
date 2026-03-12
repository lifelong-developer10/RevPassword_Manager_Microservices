package com.example.revpassword_manager.DTOs;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
