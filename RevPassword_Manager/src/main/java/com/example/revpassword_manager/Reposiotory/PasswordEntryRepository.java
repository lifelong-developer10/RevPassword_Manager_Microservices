package com.example.revpassword_manager.Reposiotory;

import com.example.revpassword_manager.Models.AllPasswordEntry;
import com.example.revpassword_manager.Models.MasterUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface PasswordEntryRepository
        extends JpaRepository<AllPasswordEntry, Long> {

    List<AllPasswordEntry> findByUser(MasterUser user);

    List<AllPasswordEntry> findByUserAndCategory(
            MasterUser user, String category);

    List<AllPasswordEntry> findByUserAndFavoriteTrue(
            MasterUser user);

    List<AllPasswordEntry> findByUserAndAccountNameContainingIgnoreCase(
            MasterUser user, String keyword);

    Optional<AllPasswordEntry>
    findTopByUserUsernameOrderByCreatedAtDesc(String username);
}

