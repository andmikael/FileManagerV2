package com.own.filemanager.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    @PostMapping("/login")
    public ResponseEntity<String> handleLogin(@RequestBody String postBody) {
        if (postBody.contains("trial")) {
            this.blobStorage.setConnString(postBody);
            if(!this.blobStorage.init()) {
                return new ResponseEntity<>("Error while accessing trial account", HttpStatus.BAD_REQUEST);
            }
        }

        this.blobStorage.setConnString(postBody);
        if(!this.blobStorage.init()) {
            return new ResponseEntity<>("Invalid connection string", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
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
