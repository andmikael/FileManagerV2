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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
    public ResponseEntity<?> handleLogout(HttpServletRequest request) {
        /*HttpSession session = request.getSession(false);
        SecurityContextHolder.clearContext();
        this.blobStorage.logout();
        if (session != null) {
            session.invalidate();
        }*/
        return new ResponseEntity<>("", HttpStatus.OK);
    }
    
    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("value of current user: " + auth);
        Gson gson = new Gson();
        Map<String, String> user = new HashMap<>();
        String json = null;
        try {
            user.put("role", auth.getAuthorities().iterator().next().toString());
            json = gson.toJson(user);
        } catch (Exception e) {
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
