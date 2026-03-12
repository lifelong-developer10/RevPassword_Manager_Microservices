package com.example.revpassword_manager.Services;

import com.example.revpassword_manager.DTOs.PasswordEntryRequest;
import com.example.revpassword_manager.DTOs.PasswordEntryResponse;
import com.example.revpassword_manager.Models.AllPasswordEntry;
import com.example.revpassword_manager.Models.MasterUser;
import com.example.revpassword_manager.Reposiotory.PasswordEntryRepository;
import com.example.revpassword_manager.Reposiotory.UserRepository;
import com.example.revpassword_manager.Security.EncryptionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasswordEntryService {

    private final PasswordEntryRepository repo;
    private final UserRepository userRepo;
    private final EncryptionUtil encryptionUtil;

    private MasterUser getUser(String username) {
        return userRepo.findByUsername(username).orElseThrow();
    }

    public PasswordEntryResponse addEntry(
            String username,
            PasswordEntryRequest request) throws Exception {

        MasterUser user = getUser(username);

        AllPasswordEntry entry = new AllPasswordEntry();

        entry.setAccountName(request.getAccountName());
        entry.setWebsite(request.getWebsite());
        entry.setUsername(request.getUsername());

        entry.setPasswordEncrypted(
                encryptionUtil.encrypt(request.getPassword()));

        entry.setCategory(request.getCategory());
        entry.setNotes(request.getNotes());
        entry.setFavorite(request.isFavorite());

        entry.setCreatedAt(LocalDateTime.now());
        entry.setUpdatedAt(LocalDateTime.now());

        entry.setUser(user);

        repo.save(entry);

        return mapToResponse(entry);
    }

    public PasswordEntryResponse getLastEntry(String username) throws Exception {

        AllPasswordEntry entry =
                repo.findTopByUserUsernameOrderByCreatedAtDesc(username)
                        .orElse(null);

        if (entry == null) return null;

        PasswordEntryResponse res = new PasswordEntryResponse();

        res.setId(entry.getId());
        res.setAccountName(entry.getAccountName());
        res.setWebsite(entry.getWebsite());
        res.setUsername(entry.getUsername());

        res.setPassword(
                encryptionUtil.decrypt(entry.getPasswordEncrypted())
        );

        res.setCategory(entry.getCategory());
        res.setNotes(entry.getNotes());
        res.setFavorite(entry.isFavorite());

        return res;
    }

    public List<PasswordEntryResponse> getAllEntries(String username)
            throws Exception {

        MasterUser user = getUser(username);

        return repo.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PasswordEntryResponse getEntry(Long id)
            throws Exception {

        AllPasswordEntry entry = repo.findById(id).orElseThrow();

        return mapToResponse(entry);
    }

    public PasswordEntryResponse updateEntry(
            Long id,
            PasswordEntryRequest request) throws Exception {

        AllPasswordEntry entry = repo.findById(id).orElseThrow();

        entry.setAccountName(request.getAccountName());
        entry.setWebsite(request.getWebsite());
        entry.setUsername(request.getUsername());

        entry.setPasswordEncrypted(
                encryptionUtil.encrypt(request.getPassword()));

        entry.setCategory(request.getCategory());
        entry.setNotes(request.getNotes());
        entry.setFavorite(request.isFavorite());
        entry.setUpdatedAt(LocalDateTime.now());

        repo.save(entry);

        return mapToResponse(entry);
    }
@Transactional
    public void deleteEntry(Long id) {
        repo.deleteById(id);
    }

    public List<PasswordEntryResponse> getFavorites(String username)
            throws Exception {

        MasterUser user = getUser(username);

        return repo.findByUserAndFavoriteTrue(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PasswordEntryResponse> search(
            String username, String keyword) throws Exception {

        MasterUser user = getUser(username);

        return repo
                .findByUserAndAccountNameContainingIgnoreCase(
                        user, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PasswordEntryResponse> filterByCategory(
            String username, String category) throws Exception {

        MasterUser user = getUser(username);

        return repo.findByUserAndCategory(user, category)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PasswordEntryResponse mapToResponse(
            AllPasswordEntry entry) {

        try {

            return PasswordEntryResponse.builder()
                    .id(entry.getId())
                    .accountName(entry.getAccountName())
                    .website(entry.getWebsite())
                    .username(entry.getUsername())
                    .password(
                            encryptionUtil.decrypt(
                                    entry.getPasswordEncrypted()))
                    .category(entry.getCategory())
                    .notes(entry.getNotes())
                    .favorite(entry.isFavorite())
                    .createdAt(entry.getCreatedAt())
                    .updatedAt(entry.getUpdatedAt())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}