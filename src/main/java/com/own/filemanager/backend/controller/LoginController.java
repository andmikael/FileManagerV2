package com.own.filemanager.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.nimbusds.jose.shaded.gson.Gson;
import com.own.filemanager.backend.service.BlobStorage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/auth")
public class LoginController {
    private final BlobStorage blobStorage;

    public LoginController(BlobStorage blobStorage) {
        this.blobStorage = blobStorage;
    }

    @GetMapping("/")
    public ResponseEntity<?> getUser() {
        if (blobStorage.getAccountType() == null) {
            return new ResponseEntity<>(null, null, HttpStatus.OK);
        } else if (blobStorage.getAccountType().contains("trial")) {
            Map<String, String> user = new HashMap<>();
            user.put("accounType","trial");
            Gson gson = new Gson();
            String json = gson.toJson(user);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            return new ResponseEntity<>(json, headers, HttpStatus.OK);
        }
        Map<String, String> user = new HashMap<>();
        user.put("accounType","user");
        Gson gson = new Gson();
        String json = gson.toJson(user);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return new ResponseEntity<>(json, headers, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> handleLogin(@RequestBody String postBody) {
        Map<String, String> user = new HashMap<>();
        if (postBody.contains("trial")) {
            this.blobStorage.setConnString(postBody);
            if(!this.blobStorage.init()) {
                return new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
            }
            user.put("accounType","trial");
        }

        this.blobStorage.setConnString(postBody);
        if(!this.blobStorage.init()) {
            return new ResponseEntity<>(null, null, HttpStatus.UNAUTHORIZED);
        } else {
            user.put("accounType","user");
        }

        Gson gson = new Gson();
        String json = gson.toJson(user);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        return new ResponseEntity<>(json, headers, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> handleLogout() {
        this.blobStorage.logout();
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    public static HttpSession resetSessionId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false); 
        session.invalidate();
        session = request.getSession(true);
        return session;
}
}
