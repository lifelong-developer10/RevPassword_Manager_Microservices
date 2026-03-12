package com.example.revpassword_manager.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class SecurityPassStrength {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime issueDate;
    private String description;
    private LocalDateTime detectedAt;

    @ManyToOne
    private MasterUser user;
}
