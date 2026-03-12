package com.example.revpassword_manager.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordStrengthResponse {

    private String strength;
    private int score;
}