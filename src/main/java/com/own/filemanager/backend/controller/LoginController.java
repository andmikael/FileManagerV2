package com.own.filemanager.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.own.filemanager.backend.service.BlobStorage;

@Controller
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

    @PostMapping(value="/loginContainer")
    public String handleLogin(@RequestBody String postBody, Model model) {
        String connectionString = postBody.substring(15, postBody.indexOf("\n")-1);
        this.blobStorage.setConnString(connectionString);
        return "redirect:/container";
    }
}
