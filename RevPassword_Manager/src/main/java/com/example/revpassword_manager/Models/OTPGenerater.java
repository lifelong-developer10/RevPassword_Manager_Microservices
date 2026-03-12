package com.example.revpassword_manager.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data

public class OTPGenerater {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String code;
        private LocalDateTime expiryTime;
        private boolean used;

        @ManyToOne
        private MasterUser user;
    }


