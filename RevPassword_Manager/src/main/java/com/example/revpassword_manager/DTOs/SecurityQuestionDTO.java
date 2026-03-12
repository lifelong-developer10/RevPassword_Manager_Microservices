package com.example.revpassword_manager.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SecurityQuestionDTO {
    private Long questionId;
    private String question;
    private String answer;
}
