package com.example.revpassword_manager.Controllers;


import com.example.revpassword_manager.DTOs.*;
import com.example.revpassword_manager.Models.MasterUser;
import com.example.revpassword_manager.Models.SecurityQuestions;
import com.example.revpassword_manager.Reposiotory.UserRepository;
import com.example.revpassword_manager.Security.CustomUserDetails;
import com.example.revpassword_manager.Services.AuthService;
import com.example.revpassword_manager.Services.ForgotPasswordService;
import com.example.revpassword_manager.Services.SecurityQuestionService;
import com.example.revpassword_manager.Services.TwoFactorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final SecurityQuestionService security;
    private final TwoFactorService twoFactorService;
    private final ForgotPasswordService forgotPasswordService;

    // ================= PROFILE =================

    @GetMapping
    public ProfileResponse getProfile(Authentication auth) {

        String username = auth.getName();

        MasterUser user =
                userRepository.findByUsername(username)
                        .orElseThrow();

        return new ProfileResponse(
                user.getUsername(),
                user.getEmail(),
                user.getPhone()
        );
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(
            Authentication auth,
            @RequestBody UpdateProfileRequest request) {

        String username = auth.getName();

        MasterUser updated =
                authService.updateProfile(username, request);

        return ResponseEntity.ok(updated);
    }
    // ================= PASSWORD =================

    @PostMapping("/change-password")
    public Map<String,String> changePassword(
            Authentication auth,
            @RequestBody ChangePasswordRequest req) {
                String username = auth.getName();

        authService.changePassword(username, req);

        return Map.of("message","Password Updated");
    }

    // ================= SECURITY QUESTIONS =================

    @GetMapping("/security-questions")
    public List<UserQuestionAnswer> getUserSecurityQuestions(
            Authentication auth) {

        String username = auth.getName();

        return forgotPasswordService.getUserQuestionsWithMask(username);
    }

    @PutMapping("/security-questions")
    public Map<String, String> updateQuestions(
            Authentication auth,
            @RequestBody List<UserQuestionAnswer> list) {

        String username = auth.getName();

        return security.updateQuestions(username, list);
    }

    // ================= 2FA =================

    @PostMapping("/2fa")
    public String update2FA(
            Authentication auth,
            @RequestBody TwoFactorRequest request) {

        String username = auth.getName();

        return twoFactorService.updateTwoFactor(
                username,
                request.isEnabled());
    }
}