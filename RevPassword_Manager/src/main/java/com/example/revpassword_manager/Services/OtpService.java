package com.example.revpassword_manager.Services;

import com.example.revpassword_manager.Models.MasterUser;
import com.example.revpassword_manager.Models.OTPGenerater;
import com.example.revpassword_manager.Reposiotory.OtpRepository;
import com.example.revpassword_manager.Reposiotory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepo;
    private final UserRepository userRepo;

    public String generateOtp(String username) {

        MasterUser user =
                userRepo.findByUsername(username).orElseThrow();

        String code = String.valueOf(100000 +
                new Random().nextInt(900000));

        OTPGenerater otp = new OTPGenerater();

        otp.setCode(code);
        otp.setUser(user);
        otp.setUsed(false);
        otp.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepo.save(otp);


        System.out.println("OTP for " + username + " = " + code);

        return "OTP sent successfully";
    }

    public boolean verifyOtp(String username, String code) {

        MasterUser user =
                userRepo.findByUsername(username).orElseThrow();

        OTPGenerater otp = otpRepo
                .findTopByUserOrderByExpiryTimeDesc(user)
                .orElseThrow();

        if (otp.isUsed())
            throw new RuntimeException("OTP already used");

        if (otp.getExpiryTime().isBefore(LocalDateTime.now()))
            throw new RuntimeException("OTP expired");

        if (!otp.getCode().equals(code))
            throw new RuntimeException("Invalid OTP");

        otp.setUsed(true);
        otpRepo.save(otp);

        return true;
    }
}