package com.own.filemanager.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.models.BlobItem;
import com.google.gson.Gson;
import com.own.filemanager.backend.service.BlobStorage;

@Controller
@RequestMapping("/api/index")
public class FileController {

    private final BlobStorage blobStorage;
    private PagedIterable<BlobItem> listOfBlobs;
    
    public FileController(BlobStorage blobStorage) {
        this.blobStorage = blobStorage;
    }

    @GetMapping(value="/")
    public ResponseEntity<?> switchControllers() {
        Map<String, List<String>> blobs = new HashMap<>();
        this.listOfBlobs = blobStorage.getBlobs();
        List<String> allBlobs = new ArrayList<>();
        for (BlobItem elem : this.listOfBlobs) {
            allBlobs.add(elem.getName());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        Gson gson = new Gson();
        blobs.put("blobs", allBlobs);
        String json = gson.toJson(blobs);
        return new ResponseEntity<>(json, headers, HttpStatus.OK);
    }

    @PostMapping(value="/uploadfile")
    public ResponseEntity<?> handleFileUpload(@RequestBody MultipartFile file) {
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
