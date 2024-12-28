package com.own.filemanager.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.azure.core.http.rest.PagedIterable;
import com.azure.core.http.rest.Response;
import com.azure.storage.blob.models.BlobContainerItem;
import com.nimbusds.jose.shaded.gson.Gson;
import com.own.filemanager.backend.service.BlobStorage;

@Controller
@RequestMapping("/api/containers")
public class ContainerController {
    private final BlobStorage blobStorage;

    public ContainerController(BlobStorage blobStorage) {
        this.blobStorage = blobStorage;
    }

    @GetMapping("/")
    public ResponseEntity<?> populateDropDown(){
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

    @PostMapping(value="/selectcontainer")
    public ResponseEntity<?> handleContainerSelection(@RequestBody String postBody) {
        blobStorage.createContainer(postBody);
        blobStorage.setContainerClient(blobStorage.getContainerClient(postBody));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value="/deletecontainer")
    public ResponseEntity<?> handleContainerDeletion(@RequestBody String postBody) {
        if (this.blobStorage.getAccountType().contains("trial")) {
            return new ResponseEntity<>("No permissions for deleting containers", HttpStatus.FORBIDDEN);
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

    @PostMapping(value="/createcontainer")
    public ResponseEntity<?> handleContainerCreation(@RequestBody String postBody) {
        if (this.blobStorage.getAccountType().contains("trial")) {
            return new ResponseEntity<>("No permission to create containers", HttpStatus.FORBIDDEN);
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
}