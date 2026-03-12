package com.example.revpassword_manager.Controllers;


import com.example.revpassword_manager.DTOs.ResetPasswordRequest;
import com.example.revpassword_manager.DTOs.SecurityQuestionDTO;
import com.example.revpassword_manager.DTOs.UserQuestionAnswer;
import com.example.revpassword_manager.DTOs.VerifySecurityAnswersRequest;
import com.example.revpassword_manager.Models.SecurityQuestionMaster;
import com.example.revpassword_manager.Reposiotory.SecurityQuestionRepository;
import com.example.revpassword_manager.Reposiotory.UserRepository;
import com.example.revpassword_manager.Security.CustomUserDetails;
import com.example.revpassword_manager.Services.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forgot")

@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService service;
    private final SecurityQuestionRepository masterRepo;
 private final UserRepository userRepo;

    @GetMapping("/user-exists/{username}")
    public boolean userExists(@PathVariable String username) {

        return userRepo.findByUsername(username).isPresent();
    }

    @GetMapping("/questions/{username}")
    public List<UserQuestionAnswer> getQuestions(
            @PathVariable String username) {

        return service.getUserQuestionsWithMask(username);
    }

    // ✅ Step 2 — Verify Answers
    @PostMapping("/verify")
    public String verify(
            @RequestBody VerifySecurityAnswersRequest request) {

        boolean result =
                service.verifyAnswers(request);

        return result ?
                "VERIFIED" :
                "INVALID_ANSWERS";
    }

    // ✅ Step 3 — Reset Password
    @PostMapping("/reset")
    public String reset(
            @RequestBody ResetPasswordRequest request) {

        return service.resetPassword(request);
    }

}