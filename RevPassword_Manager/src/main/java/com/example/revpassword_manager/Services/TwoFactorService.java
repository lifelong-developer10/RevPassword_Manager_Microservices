package com.example.revpassword_manager.Services;

import com.example.revpassword_manager.Models.MasterUser;
import com.example.revpassword_manager.Reposiotory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TwoFactorService {

    private final UserRepository userRepo;

    public String updateTwoFactor(String username, boolean enabled) {

        MasterUser user =
                userRepo.findByUsername(username).orElseThrow();

        user.setTwoFactorEnabled(enabled);

        userRepo.save(user);

        return enabled ?
                "2FA Enabled Successfully" :
                "2FA Disabled Successfully";
    }

    public boolean isTwoFactorEnabled(String username) {

        MasterUser user =
                userRepo.findByUsername(username).orElseThrow();

        return user.isTwoFactorEnabled();
    }
}
