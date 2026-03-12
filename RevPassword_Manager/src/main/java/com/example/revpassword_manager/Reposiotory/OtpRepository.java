package com.example.revpassword_manager.Reposiotory;

import com.example.revpassword_manager.Models.MasterUser;
import com.example.revpassword_manager.Models.OTPGenerater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface OtpRepository extends JpaRepository<OTPGenerater,Long> {
    Optional<OTPGenerater> findTopByUserOrderByExpiryTimeDesc(MasterUser user);

}
