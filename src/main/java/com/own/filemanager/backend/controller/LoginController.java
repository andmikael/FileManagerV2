package com.own.filemanager.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.own.filemanager.backend.service.BlobStorage;

@RestController
@CrossOrigin("http://localhost:8080")
public class LoginController {
    private final BlobStorage blobStorage;

    @Autowired
    public LoginController(BlobStorage blobStorage) {
        this.blobStorage = blobStorage;
    }

    @GetMapping("/")
    public String initPage(Model model, RedirectAttributes redirectAttrs){
        return "login";
    }

    @PostMapping("/api/login")
    public ResponseEntity<String> handleLogin(@RequestBody String postBody, Model model) {
        this.blobStorage.setConnString(postBody);
        if(!this.blobStorage.init()) {
            return new ResponseEntity<>("Invalid connection string", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
