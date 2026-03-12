package com.example.revpassword_manager.Services;

import com.example.revpassword_manager.DTOs.*;
import com.example.revpassword_manager.Models.MasterUser;
import com.example.revpassword_manager.Models.SecurityQuestionMaster;
import com.example.revpassword_manager.Models.SecurityQuestions;
import com.example.revpassword_manager.Reposiotory.SecurityQuestionMasterRepository;
import com.example.revpassword_manager.Reposiotory.SecurityQuestionRepository;
import com.example.revpassword_manager.Reposiotory.UserRepository;

import com.example.revpassword_manager.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor

public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    private final UserRepository userRepository; // Your JPA Repository
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;// Injected from SecurityConfig
    private final SecurityQuestionRepository userQuestionRepo;
    private final SecurityQuestionMasterRepository masterRepo;
    public String register(RegisterRequest request) {

        if (request.getSecurityAnswers() == null ||
                request.getSecurityAnswers().size() != 3) {
            throw new RuntimeException("Select exactly 3 questions");
        }

        MasterUser user = new MasterUser();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordEncrypted(
                passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        for (UserQuestionAnswer dto : request.getSecurityAnswers()) {

            if (dto.getQuestionId() == null) {
                throw new RuntimeException("Question ID cannot be null");
            }

            SecurityQuestionMaster master =
                    masterRepo.findById(dto.getQuestionId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Invalid Question ID: "
                                                    + dto.getQuestionId()));

            SecurityQuestions uq = new SecurityQuestions();

            uq.setUser(user);
            uq.setQuestion(master);
            uq.setAnswerHash(
                    passwordEncoder.encode(dto.getAnswer()));

            userQuestionRepo.save(uq);
        }

        return "User Registered Successfully";
    }

    public String login(LoginRequest request) {

        MasterUser user =
                userRepository.findByUsername(request.getUsername())
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordEncrypted())) {

            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getUsername());
    }



    public String changePassword(String username,
                                 ChangePasswordRequest req) {

        MasterUser user =
                userRepository.findByUsername(username).orElseThrow();

        if (!passwordEncoder.matches(req.getCurrentPassword(),
                user.getPasswordEncrypted())) {

            throw new RuntimeException("Wrong current password");
        }

        user.setPasswordEncrypted(
                passwordEncoder.encode(req.getNewPassword()));

        userRepository.save(user);

        return "Password Updated";
    }

    @Transactional
    public MasterUser updateProfile(String username,
                                    UpdateProfileRequest req) {

        MasterUser user =
                userRepository.findByUsername(username)
                        .orElseThrow();

        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());

        return userRepository.save(user);
    }

    public List<SecurityQuestionMaster> getAllQuestions() {
        return masterRepo.findAll();
    }
}
