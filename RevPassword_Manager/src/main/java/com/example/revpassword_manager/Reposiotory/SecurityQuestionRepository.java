package com.example.revpassword_manager.Reposiotory;

import com.example.revpassword_manager.Models.MasterUser;
import com.example.revpassword_manager.Models.SecurityQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestions,Long> {
    List<SecurityQuestions> findByUser(MasterUser user);
    void deleteByUser(MasterUser user);
}
