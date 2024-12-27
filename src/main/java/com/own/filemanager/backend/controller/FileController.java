package com.own.filemanager.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.models.BlobItem;
import com.google.gson.Gson;
import com.own.filemanager.backend.service.BlobStorage;

@Controller
@RequestMapping("/api/index")
public class FileController {

    private final BlobStorage blobStorage;
    private PagedIterable<BlobItem> listOfBlobs;
    
    @Autowired
    public FileController(BlobStorage blobStorage) {
        this.blobStorage = blobStorage;
    }

    @GetMapping(value="/")
    public ResponseEntity<?> switchControllers(Model model, RedirectAttributes redirectAttribs){
        if(!this.blobStorage.init()) {
            System.out.println("unauthorized");
            return new ResponseEntity<>("unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (blobStorage.getCurrentContainerClient() == null) {
            System.out.println("unauthorized");
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        String containerName = blobStorage.getCurrentContainerClient().getBlobContainerName();
        this.listOfBlobs = blobStorage.getBlobs();
        List<String> allBlobs = new ArrayList<>();
        for (BlobItem elem : this.listOfBlobs) {
            allBlobs.add(elem.getName());
        }
        Gson gson = new Gson();
        String json = gson.toJson(allBlobs);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @PostMapping(value="/uploadfile")
    public ResponseEntity<?> handleFileUpload(@RequestBody MultipartFile file) {
        if(!this.blobStorage.init()) {
            return new ResponseEntity<>("unauthorized", HttpStatus.UNAUTHORIZED);
        }

        String result = blobStorage.uploadFile(file, file.getOriginalFilename());
        switch (result) {
            case "error" -> {
                return new ResponseEntity<>("Encountered an error while trying to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            case "too-large" -> {
                return new ResponseEntity<>("File size exceeds 5MB", HttpStatus.PAYLOAD_TOO_LARGE);
            }
            default -> {
                return new ResponseEntity<>("OK", HttpStatus.OK);
            }
        }
    }

    /*
     * TODO: add ability to rename files before upload
     */ 
}
