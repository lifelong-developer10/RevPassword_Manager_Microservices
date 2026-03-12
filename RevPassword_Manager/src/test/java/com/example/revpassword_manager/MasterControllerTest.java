package com.example.revpassword_manager;

import com.example.revpassword_manager.Controllers.MasterController;
import com.example.revpassword_manager.DTOs.*;
import com.example.revpassword_manager.Models.MasterUser;
import com.example.revpassword_manager.Models.SecurityQuestionMaster;
import com.example.revpassword_manager.Reposiotory.UserRepository;
import com.example.revpassword_manager.Services.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MasterControllerTest {

    private AuthService authService;
    private UserRepository userRepository;
    private MasterController controller;

    @BeforeEach
    void setup() {
        authService = mock(AuthService.class);
        userRepository = mock(UserRepository.class);

        controller = new MasterController(
                authService,
                null,
                userRepository,
                authService,
                null
        );
    }

    // ================= REGISTER =================

    @Test
    void register_success() {

        RegisterRequest req = new RegisterRequest();
        req.setUsername("Shubhada");

        when(authService.register(any()))
                .thenReturn("User Registered Successfully");

        var response = controller.register(req);

        assertEquals(200, response.getStatusCode().value());

        Map body = (Map) response.getBody();
        assertEquals("User Registered Successfully", body.get("message"));
    }

    // ================= LOGIN =================

    @Test
    void login_success() {

        LoginRequest req = new LoginRequest();
        req.setUsername("Shubhada");
        req.setPassword("123");

        when(authService.login(any()))
                .thenReturn("mock-token");

        var response = controller.login(req);

        assertEquals("mock-token", response.getBody().getToken());
        assertEquals("Login Successful", response.getBody().getMessage());
    }

    // ================= PROFILE =================

    @Test
    void getProfile_success() {

        MasterUser user = new MasterUser();
        user.setUsername("Shubhada");
        user.setEmail("test@gmail.com");
        user.setPhone("9999999999");

        when(userRepository.findByUsername("Shubhada"))
                .thenReturn(Optional.of(user));

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("Shubhada");

        ProfileResponse response = controller.getProfile(auth);

        assertEquals("Shubhada", response.getUsername());
        assertEquals("test@gmail.com", response.getEmail());
        assertEquals("9999999999", response.getPhone());
    }

    // ================= SECURITY QUESTIONS =================

    @Test
    void getAllQuestions_success() {

        SecurityQuestionMaster q = new SecurityQuestionMaster();
        q.setId(1L);
        q.setQuestion("Pet name?");

        when(authService.getAllQuestions())
                .thenReturn(List.of(q));

        List<SecurityQuestionMaster> result =
                controller.getAllQuestions();

        assertEquals(1, result.size());
        assertEquals("Pet name?", result.get(0).getQuestion());
    }
}