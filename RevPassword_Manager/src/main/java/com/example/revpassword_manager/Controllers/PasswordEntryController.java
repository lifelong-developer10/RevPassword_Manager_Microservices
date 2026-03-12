package com.example.revpassword_manager.Controllers;

import com.example.revpassword_manager.DTOs.PasswordEntryRequest;
import com.example.revpassword_manager.DTOs.PasswordEntryResponse;
import com.example.revpassword_manager.Models.AllPasswordEntry;
import com.example.revpassword_manager.Security.CustomUserDetails;
import com.example.revpassword_manager.Services.PasswordEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/vault")
@RequiredArgsConstructor
public class PasswordEntryController {

    private final PasswordEntryService service;

    @PostMapping
    public PasswordEntryResponse add(
            Authentication auth,
            @RequestBody PasswordEntryRequest request)
            throws Exception {

        String username = auth.getName();
        return service.addEntry(username, request);
    }

    @GetMapping
    public List<PasswordEntryResponse> getAll(
            Authentication auth)
            throws Exception {

        String username = auth.getName();
        return service.getAllEntries(username);
    }

    @GetMapping("/{id}")
    public PasswordEntryResponse getOne(@PathVariable Long id)
            throws Exception {

        return service.getEntry(id);
    }

    @PutMapping("/{id}")
    public PasswordEntryResponse update(
            @PathVariable Long id,
            @RequestBody PasswordEntryRequest request)
            throws Exception {

        return service.updateEntry(id, request);
    }

    @GetMapping("/last")
    public PasswordEntryResponse getLast(Authentication auth)
            throws Exception {

        return service.getLastEntry(auth.getName());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteEntry(id);
    }

    @GetMapping("/favorites")
    public List<PasswordEntryResponse> favorites(
            Authentication auth)
            throws Exception {

        return service.getFavorites(auth.getName());
    }

    @GetMapping("/search")
    public List<PasswordEntryResponse> search(
            Authentication auth,
            @RequestParam String keyword)
            throws Exception {

        return service.search(auth.getName(), keyword);
    }

    @GetMapping("/category")
    public List<PasswordEntryResponse> filter(
            Authentication auth,
            @RequestParam String category)
            throws Exception {

        return service.filterByCategory(
                auth.getName(), category);
    }
}