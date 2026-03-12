package com.example.revpassword_manager;


import com.example.revpassword_manager.Controllers.ProfileController;
import com.example.revpassword_manager.DTOs.*;
import com.example.revpassword_manager.Models.MasterUser;
import com.example.revpassword_manager.Reposiotory.UserRepository;
import com.example.revpassword_manager.Services.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileControllerTest {

    private UserRepository userRepository;
    private AuthService authService;
    private SecurityQuestionService securityService;
    private TwoFactorService twoFactorService;
    private ForgotPasswordService forgotPasswordService;

    private ProfileController controller;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        authService = mock(AuthService.class);
        securityService = mock(SecurityQuestionService.class);
        twoFactorService = mock(TwoFactorService.class);
        forgotPasswordService = mock(ForgotPasswordService.class);

        controller = new ProfileController(
                userRepository,
                authService,
                securityService,
                twoFactorService,
                forgotPasswordService
        );
    }

    private Authentication mockAuth(String username) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(username);
        return auth;
    }

    // ================= GET PROFILE =================

    @Test
    void getProfile_success() {

        MasterUser user = new MasterUser();
        user.setUsername("Shubhu");
        user.setEmail("test@gmail.com");
        user.setPhone("9999999999");

        when(userRepository.findByUsername("Shubhu"))
                .thenReturn(Optional.of(user));

        ProfileResponse response =
                controller.getProfile(mockAuth("Shubhu"));

        assertEquals("teju", response.getUsername());
        assertEquals("test@gmail.com", response.getEmail());
        assertEquals("9999999999", response.getPhone());
    }

    // ================= UPDATE PROFILE =================

    @Test
    void updateProfile_success() {

        UpdateProfileRequest req = new UpdateProfileRequest();
        req.setEmail("new@gmail.com");
        req.setPhone("8888888888");

        MasterUser updated = new MasterUser();
        updated.setUsername("Shubhu");
        updated.setEmail("new@gmail.com");
        updated.setPhone("8888888888");

        when(authService.updateProfile(eq("Shubhu"), any()))
                .thenReturn(updated);

        var response = controller.updateProfile(
                mockAuth("Shubhu"), req);

        assertEquals(200, response.getStatusCode().value());
        MasterUser body = (MasterUser) response.getBody();

        assertEquals("new@gmail.com", body.getEmail());
        assertEquals("8888888888", body.getPhone());
    }

    // ================= CHANGE PASSWORD =================

    @Test
    void changePassword_success() {

        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setCurrentPassword("old");
        req.setNewPassword("new");

        when(authService.changePassword(eq("Shubhu"), any()))
                .thenReturn("Password Updated");

        Map<String, String> result =
                controller.changePassword(mockAuth("Shubhu"), req);

        assertEquals("Password Updated", result.get("message"));
    }

    // ================= GET USER SECURITY QUESTIONS =================

    @Test
    void getUserSecurityQuestions_success() {

        UserQuestionAnswer q = new UserQuestionAnswer();
        q.setQuestionId(1L);
        q.setAnswer("****");

        when(forgotPasswordService
                .getUserQuestionsWithMask("teju"))
                .thenReturn(List.of(q));

        List<UserQuestionAnswer> result =
                controller.getUserSecurityQuestions(
                        mockAuth("teju"));

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getQuestionId());
    }

    // ================= UPDATE QUESTIONS =================

    @Test
    void updateQuestions_success() {

        UserQuestionAnswer q = new UserQuestionAnswer();
        q.setQuestionId(1L);
        q.setAnswer("dog");

        when(securityService.updateQuestions(
                eq("teju"), any()))
                .thenReturn(Map.of("message", "Updated"));

        Map<String, String> result =
                controller.updateQuestions(
                        mockAuth("teju"),
                        List.of(q));

        assertEquals("Updated", result.get("message"));
    }

    // ================= UPDATE 2FA =================

    @Test
    void update2FA_success() {

        TwoFactorRequest req = new TwoFactorRequest();
        req.setEnabled(true);

        when(twoFactorService.updateTwoFactor("teju", true))
                .thenReturn("2FA Enabled");

        String result =
                controller.update2FA(mockAuth("teju"), req);

        assertEquals("2FA Enabled", result);
    }
}