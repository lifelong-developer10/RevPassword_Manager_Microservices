package com.example.revpassword_manager.DTOs;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String username;
    private String newPassword;

}
