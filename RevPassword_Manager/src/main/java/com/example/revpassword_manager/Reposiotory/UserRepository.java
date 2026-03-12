package com.example.revpassword_manager.Reposiotory;

import com.example.revpassword_manager.Models.MasterUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MasterUser, Long> {
    Optional<MasterUser> findByUsername(String username);

    Optional<MasterUser> findByEmail(String email);

    boolean existsByUsername(String username);
}
