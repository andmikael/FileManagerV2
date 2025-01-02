package com.own.filemanager.backend.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.nimbusds.jose.shaded.gson.Gson;
import com.own.filemanager.backend.service.BlobStorage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@SessionScope
@RequestMapping("/api/auth")
public class LoginController {
    private final BlobStorage blobStorage;
    //private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public LoginController(BlobStorage blobStorage) {
        this.blobStorage = blobStorage;
    }

    @GetMapping("/")
    public ResponseEntity<?> getUser() {
        //HttpHeaders headers = new HttpHeaders();
        //headers.set("Content-Type", "application/json");
        Gson gson = new Gson();
        Map<String, String> user = new HashMap<>();


        String json = gson.toJson(user);

        return new ResponseEntity<>("", HttpStatus.OK);

        /*if (blobStorage.getAccountType() == null) {
            Map<String, String> user = new HashMap<>();
            user.put("accounType","none");
            String json = gson.toJson(user);
            //return new ResponseEntity<>(json, headers, HttpStatus.OK);
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else if (blobStorage.getAccountType().contains("trial")) {
            System.out.println("account is trial");
            Map<String, String> user = new HashMap<>();
            user.put("accounType","trial");
            String json = gson.toJson(user);
            //return new ResponseEntity<>(json, headers, HttpStatus.OK);
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
        Map<String, String> user = new HashMap<>();
        System.out.println("account is user");
        user.put("accounType","user");
        String json = gson.toJson(user);
        //return new ResponseEntity<>(json, headers, HttpStatus.OK);
        return new ResponseEntity<>(json, HttpStatus.OK);
    */

    }

    @PostMapping("/")
    public ResponseEntity<String> handleLogin() {
        /*String postBody = "";
        Map<String, String> user = new HashMap<>();
        if (postBody.contains("trial")) {
            this.blobStorage.setConnString(postBody);
            if(!this.blobStorage.init()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            user.put("accounType","trial");
        }

        this.blobStorage.setConnString(postBody);
        if(!this.blobStorage.init()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            user.put("accounType","user");
        }

        Gson gson = new Gson();
        String json = gson.toJson(user);
        //HttpHeaders headers = new HttpHeaders();
        //headers.set("Content-Type", "application/json");

        //return new ResponseEntity<>(json, headers, HttpStatus.OK);*/
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
        System.out.println(auth);
        Gson gson = new Gson();
        Map<String, String> user = new HashMap<>();
        if (auth == null || auth.getAuthorities().iterator().next().toString().equals("ROLE_ANONYMOUS")) {
            user.put("role", "none");
        } else {
            user.put("role", auth.getAuthorities().iterator().next().toString());
        }
        String json = gson.toJson(user);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
