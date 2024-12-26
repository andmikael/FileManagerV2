package com.own.filemanager.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.own.filemanager.backend.service.BlobStorage;

@Controller
public class LoginController {
    private final BlobStorage blobStorage;

    @Autowired
    public LoginController(BlobStorage blobStorage) {
        this.blobStorage = blobStorage;
    }

    @PostMapping("/api/login")
    public ResponseEntity<String> handleLogin(@RequestBody String postBody) {
        this.blobStorage.setConnString(postBody);
        if(!this.blobStorage.init()) {
            return new ResponseEntity<>("Invalid connection string", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
