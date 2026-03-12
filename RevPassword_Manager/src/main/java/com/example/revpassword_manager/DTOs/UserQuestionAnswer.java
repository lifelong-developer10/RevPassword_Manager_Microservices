package com.example.revpassword_manager.DTOs;

import lombok.Data;

@Data
public class UserQuestionAnswer {

    private Long questionId;
    private String question;
    private String answer;
}