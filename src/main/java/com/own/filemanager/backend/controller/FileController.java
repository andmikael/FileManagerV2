package com.own.filemanager.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.models.BlobItem;
import com.google.gson.Gson;
import com.own.filemanager.backend.service.BlobStorage;
import com.own.filemanager.backend.service.FileStorage;
import com.own.filemanager.backend.service.StorageFileNotFoundException;


@Controller
@CrossOrigin("http://localhost:8080")
public class FileController {

    private final FileStorage fileStorage;
    private final BlobStorage blobStorage;
    private PagedIterable<BlobItem> listOfBlobs;
    
    @Autowired
    public FileController(FileStorage fileStorage, BlobStorage blobStorage) {
        this.fileStorage = fileStorage;
        this.blobStorage = blobStorage;
    }

    @GetMapping(value="/api/index")
    public ResponseEntity<?> switchControllers(Model model, RedirectAttributes redirectAttribs){
        if (blobStorage.getCurrentContainerClient() == null) {
            System.out.println("no container selected");
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

    @PostMapping(value="/api/index/uploadfile")
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

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?>
    handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
    
}
