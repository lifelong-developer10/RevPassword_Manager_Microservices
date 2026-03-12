package com.example.revpassword_manager.DTOs;

import lombok.Data;

@Data
public class OtpVerifyRequest {

    private String username;
    private String code;

}