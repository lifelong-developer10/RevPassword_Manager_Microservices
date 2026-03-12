package com.example.revpassword_manager.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class AllPasswordEntry {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String accountName;
        private String website;
        private String username;
        private String passwordEncrypted;
        private String category;
        private String notes;
        private boolean favorite;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "user_id")
        private MasterUser user;
    }


