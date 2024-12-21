package com.own.filemanager.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.models.BlobContainerItem;
import com.own.filemanager.backend.service.BlobStorage;
import com.own.filemanager.backend.service.StorageFileNotFoundException;

@Controller
@CrossOrigin("http://localhost:8080")
public class ContainerController {
    private final BlobStorage blobStorage;

    @Autowired
    public ContainerController(BlobStorage blobStorage) {
        this.blobStorage = blobStorage;
    }

    @GetMapping("/container")
    public String populateDropDown(Model model, RedirectAttributes redirectAttrs){
        if (!this.blobStorage.init()) {
            redirectAttrs.addFlashAttribute("message", "Wrong connection string!");
            return "redirect:/";
        }
        List<String> blobs = new ArrayList<>();
        PagedIterable<BlobContainerItem> foundBlobs = blobStorage.getBlobContainers();
        for (BlobContainerItem elem : foundBlobs) {
            blobs.add(elem.getName());
        model.addAttribute("containers", blobs);
        }
        return "container";
    }

    @PostMapping(value="/selectContainer")
    public String handleContainerSelection(@RequestBody String postBody, Model model, RedirectAttributes redirectAttribs) {
        if (postBody.contains("delete-button")) {
            if (postBody.contains("container-name=")) {
                String containerName = postBody.substring(15, postBody.indexOf("\n")-1);
                blobStorage.createContainer(containerName);
                blobStorage.setContainerClient(blobStorage.getContainerClient(containerName));
                if (blobStorage.deleteContainer()) {
                    redirectAttribs.addFlashAttribute("message", "Container deleted");
                    return "redirect:/container";
                } else {
                    redirectAttribs.addFlashAttribute("message", "Was unable to delete selected container");
                    return "redirect:/container";
                }
            } else {
                redirectAttribs.addFlashAttribute("message", "No container selected");
                return "redirect:/container";
            }
        }
        if (postBody.contains("select-button")) {
            if (postBody.contains("container-name=")) {
                String containerName = postBody.substring(15, postBody.indexOf("\n")-1);
                blobStorage.createContainer(containerName); // createContainer method returns a handle to an existing container if it already exists
            } else {
                redirectAttribs.addFlashAttribute("message", "No container selected");
                return "redirect:/container";
            }
        }
        return "redirect:/index";
    }

    @PostMapping(value="/createContainer")
    public String handleContainerCreation(@RequestBody String postBody, Model model) {
        String containerName = postBody.substring(15, postBody.indexOf("\n")-1);
        blobStorage.createContainer(containerName);
        return "redirect:/index";
    }
    
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?>
    handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}