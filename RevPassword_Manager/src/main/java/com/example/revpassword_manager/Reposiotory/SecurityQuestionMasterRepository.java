package com.example.revpassword_manager.Reposiotory;

import com.example.revpassword_manager.Models.MasterUser;
import com.example.revpassword_manager.Models.SecurityQuestionMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityQuestionMasterRepository
        extends JpaRepository<SecurityQuestionMaster, Long> {

}