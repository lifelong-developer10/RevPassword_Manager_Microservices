package com.example.revpassword_manager.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PasswordGenerateResponse {

    private List<String> passwords;

}
