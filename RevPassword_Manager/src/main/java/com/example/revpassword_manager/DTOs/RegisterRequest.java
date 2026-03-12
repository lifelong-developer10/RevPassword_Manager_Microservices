package com.example.revpassword_manager.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class RegisterRequest {

    private String username;
    private String email;
    private String phone;
    private String password;

    private List<UserQuestionAnswer> securityAnswers;
}