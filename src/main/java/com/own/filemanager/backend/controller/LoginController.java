package com.own.filemanager.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.own.filemanager.backend.service.BlobStorage;

@Controller
@RequestMapping("/api/login")
public class LoginController {
    private final BlobStorage blobStorage;

    public LoginController(BlobStorage blobStorage) {
        this.blobStorage = blobStorage;
    }

    @PostMapping("/")
    public ResponseEntity<String> handleLogin(@RequestBody String postBody) {
        this.blobStorage.setConnString(postBody);
        if(!this.blobStorage.init()) {
            return new ResponseEntity<>("Invalid connection string", HttpStatus.UNAUTHORIZED);
        }

        if (postBody.contains("trial")) {
            this.blobStorage.setConnString(postBody);
            if(!this.blobStorage.init()) {
                return new ResponseEntity<>("Error while accessing trial account", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
