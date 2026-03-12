package com.example.revpassword_manager.Controllers;

import com.example.revpassword_manager.DTOs.OtpRequest;
import com.example.revpassword_manager.DTOs.OtpVerifyRequest;
import com.example.revpassword_manager.DTOs.TwoFactorRequest;
import com.example.revpassword_manager.Security.CustomUserDetails;
import com.example.revpassword_manager.Services.OtpService;
import com.example.revpassword_manager.Services.TwoFactorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;
    private final TwoFactorService twoFactorService;

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestBody OtpRequest request) {

        String msg = otpService.generateOtp(request.getUsername());

        return ResponseEntity.ok(msg);
    }
    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody OtpVerifyRequest request) {

        boolean result =
                otpService.verifyOtp(
                        request.getUsername(),
                        request.getCode());

        return ResponseEntity.ok(
                result ? "OTP Verified" : "Invalid OTP");
    }


}