package com.example.revpassword_manager.DTOs;

import lombok.Data;
import org.jspecify.annotations.Nullable;

@Data
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;



}