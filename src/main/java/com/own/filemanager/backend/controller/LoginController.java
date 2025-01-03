package com.own.filemanager.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;

import com.nimbusds.jose.shaded.gson.Gson;
import com.own.filemanager.backend.service.BlobStorage;
import com.own.filemanager.backend.service.UserService;

@Controller
@SessionScope
@RequestMapping("/api/auth")
public class LoginController {
    private final BlobStorage blobStorage;

    public LoginController(BlobStorage blobStorage) {
        this.blobStorage = blobStorage;
    }

    @PostMapping("/login")
    public ResponseEntity<String> handleLogin() {
        if (!UserService.isUserAuthenticated()) {
            return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> handleLogout() {
        this.blobStorage.logout();
        return new ResponseEntity<>("", HttpStatus.OK);
    }
    
    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Gson gson = new Gson();
        Map<String, String> user = new HashMap<>();
        try {
            user.put("role", auth.getAuthorities().iterator().next().toString());
        } catch (Exception e) {
            user.put("role", "none");
        }
        String json = gson.toJson(user);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
