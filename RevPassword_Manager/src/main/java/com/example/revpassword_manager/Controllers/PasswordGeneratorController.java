package com.example.revpassword_manager.Controllers;

import com.example.revpassword_manager.DTOs.PasswordGenerateRequest;
import com.example.revpassword_manager.DTOs.PasswordGenerateResponse;
import com.example.revpassword_manager.DTOs.PasswordStrengthResponse;
import com.example.revpassword_manager.Services.PasswordGeneratorService;
import com.example.revpassword_manager.Services.PasswordStrengthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordGeneratorController {

    private final PasswordGeneratorService generatorService;
    private final PasswordStrengthService strengthService;

    @PostMapping("/generate")
    public PasswordGenerateResponse generate(
            @RequestBody PasswordGenerateRequest request) {

        List<String> passwords =
                generatorService.generatePasswords(request);

        return new PasswordGenerateResponse(passwords);
    }

    @GetMapping("/strength")
    public PasswordStrengthResponse strength(
            @RequestParam String password) {

        return strengthService.analyze(password);
    }
}