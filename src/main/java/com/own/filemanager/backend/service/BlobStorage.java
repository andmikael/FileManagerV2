package com.own.filemanager.backend.service;

import org.springframework.web.multipart.MultipartFile;

import com.azure.core.http.rest.PagedIterable;
import com.azure.core.http.rest.Response;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobContainerItem;
import com.azure.storage.blob.models.BlobItem;

public interface BlobStorage {
    Boolean init();
    Boolean init(String type);
    String getAccountType();
    Boolean getClientState();
    void logout();
    void setConnString(String connString) throws java.lang.IllegalArgumentException;
    PagedIterable<BlobContainerItem> fetchBlobContainers();
    PagedIterable<BlobContainerItem> getBlobContainers();
    BlobContainerClient getContainerClient(String containerName);
    void setContainerClient(BlobContainerClient containerClient);
    String getContainerName();
    Response<?> createContainer(String containerName);
    BlobContainerClient getCurrentContainerClient();
    PagedIterable<BlobItem> getBlobs();
    Response<?> deleteContainer(String containerName);
    String uploadFile(MultipartFile file, String filename);
}
