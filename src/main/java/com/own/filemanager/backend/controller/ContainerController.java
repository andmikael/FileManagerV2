package com.own.filemanager.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.azure.core.http.rest.PagedIterable;
import com.azure.core.http.rest.Response;
import com.azure.storage.blob.models.BlobContainerItem;
import com.nimbusds.jose.shaded.gson.Gson;
import com.own.filemanager.backend.service.BlobStorage;
import com.own.filemanager.backend.service.StorageFileNotFoundException;

@Controller
public class ContainerController {
    private final BlobStorage blobStorage;

    @Autowired
    public ContainerController(BlobStorage blobStorage) {
        this.blobStorage = blobStorage;
    }

    @GetMapping("/api/container")
    public ResponseEntity<?> populateDropDown(){
        if(!this.blobStorage.init()) {
            return new ResponseEntity<>("unauthorized", HttpStatus.UNAUTHORIZED);
        }

        List<String> containers = new ArrayList<>();
        PagedIterable<BlobContainerItem> foundContainers = blobStorage.getBlobContainers();
        if (foundContainers == null) {
            return new ResponseEntity<>("", HttpStatus.OK);
        }
        for (BlobContainerItem elem : foundContainers) {
            containers.add(elem.getName());  
        }
        Gson gson = new Gson();
        String json = gson.toJson(containers);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @PostMapping(value="/api/selectcontainer")
    public ResponseEntity<?> handleContainerSelection(@RequestBody String postBody) {
        if(!this.blobStorage.init()) {
            return new ResponseEntity<>("unauthorized", HttpStatus.UNAUTHORIZED);
        }

        blobStorage.createContainer(postBody);
        blobStorage.setContainerClient(blobStorage.getContainerClient(postBody));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value="/api/deletecontainer")
    public ResponseEntity<?> handleContainerDeletion(@RequestBody String postBody) {
        if(!this.blobStorage.init()) {
            return new ResponseEntity<>("unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (postBody.isEmpty()) {
            return new ResponseEntity<>("Container doesnt exist", HttpStatus.NOT_FOUND);
        }
        int statusCode = 0;
        try {
            Response<?> result = blobStorage.deleteContainer(postBody);
            statusCode = result.getStatusCode();
        } catch (Exception e) {
            if (e.getMessage().contains("404")) {
                statusCode = 404;
            }
        }
        if (statusCode == 404) {
            return new ResponseEntity<>("Container doesnt exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Container Deleted", HttpStatus.ACCEPTED);
    }

    @PostMapping(value="/api/createcontainer")
    public ResponseEntity<?> handleContainerCreation(@RequestBody String postBody) {
        if(!this.blobStorage.init()) {
            return new ResponseEntity<>("unauthorized", HttpStatus.UNAUTHORIZED);
        }

        int statusCode = 0;
        try {
            Response<?> result = blobStorage.createContainer(postBody);
            statusCode = result.getStatusCode();
        } catch (Exception e) {
            if (e.getMessage().contains("400")) {
                return new ResponseEntity<>("Container name contains illegal characters.",HttpStatus.BAD_REQUEST);
            }
        }
        if (statusCode == 409) {
            return new ResponseEntity<>("Container already exists", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("Container created", HttpStatus.CREATED);
    }
    
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?>
    handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}