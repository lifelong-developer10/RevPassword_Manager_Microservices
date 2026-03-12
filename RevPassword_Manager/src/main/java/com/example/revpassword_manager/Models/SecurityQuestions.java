package com.example.revpassword_manager.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class SecurityQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answerHash;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MasterUser user;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private SecurityQuestionMaster question;
}
