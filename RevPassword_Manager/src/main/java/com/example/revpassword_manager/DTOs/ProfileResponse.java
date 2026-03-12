package com.example.revpassword_manager.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {

    private String username;
    private String email;
    private String phone;
}