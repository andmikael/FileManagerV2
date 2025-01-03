package com.own.filemanager.backend.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.multipart.MultipartFile;

import com.azure.core.http.rest.PagedIterable;
import com.azure.core.http.rest.Response;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobContainerItem;
import com.azure.storage.blob.models.BlobItem;

@SessionScope()
public class BlobStorageService implements BlobStorage {
    private PagedIterable<BlobContainerItem> listOfBlobContainers;
    private String connectionString = null;
    private String accountType = null;
    @SuppressWarnings("unused")
    private String urlPrefix = null;
    private BlobServiceClient client = null;
    private BlobContainerClient containerClient = null;
    
    public BlobStorageService(BlobProperties properties) {
        this.connectionString = properties.getConnectionStr();
    }

    @Override
    public void setConnString(String connString) {
        this.connectionString = connString;
    }   

    @Override 
    public Boolean init() {
        if (this.connectionString.contains("trial")) {
            try {
                this.client = new BlobServiceClientBuilder()
                .endpoint(System.getenv("AZURE_TRIAL_STORAGE_ENDPOINT"))
                .connectionString(System.getenv("TRIAL_CONN_STRING"))
                .buildClient();
                this.accountType = "trial";
            } catch(java.lang.IllegalArgumentException e) {
                return false;
            }

        } else {
            try {
                this.client = new BlobServiceClientBuilder()
                .endpoint(System.getenv("AZURE_STORAGE_URL_ENDPOINT"))
                .connectionString(this.connectionString)
                .buildClient();
                this.accountType = "user";
            } catch(java.lang.IllegalArgumentException e) {
                return false;
            }
        }

        listOfBlobContainers = this.fetchBlobContainers();
        return true;
    }

    @Override
    public Boolean init(String connString) {
        System.out.println(connString);
        if (connString == null) {
            return false;
        }
        if (connString.contains("trial")) {
            try {
                this.client = new BlobServiceClientBuilder()
                .endpoint(System.getenv("AZURE_TRIAL_STORAGE_ENDPOINT"))
                .connectionString(System.getenv("TRIAL_CONN_STRING"))
                .buildClient();
                this.accountType = "trial";
            } catch(java.lang.IllegalArgumentException e) {
                return false;
            }
        } else {
            try {
                this.client = new BlobServiceClientBuilder()
                .endpoint(System.getenv("AZURE_STORAGE_URL_ENDPOINT"))
                .connectionString(connString)
                .buildClient();
                this.accountType = "user";
            } catch(Exception e) {
                return false;
            }
        }
        listOfBlobContainers = this.fetchBlobContainers();
        return true;
    }

    @Override
    public void logout() {
        this.client = null;
        this.containerClient = null;
        this.urlPrefix = null;
        this.connectionString = null;
    }

    @Override
    public Boolean getClientState() {
        return this.client != null;
    }

    @Override
    public String getAccountType() {
        return this.accountType;
    }

    @Override
    public PagedIterable<BlobContainerItem> fetchBlobContainers() {
        return client.listBlobContainers();
    }

    @Override
    public PagedIterable<BlobContainerItem> getBlobContainers() {
        return this.listOfBlobContainers;
    }

    @Override
    public BlobContainerClient getContainerClient(String containerName) {
        return this.client.getBlobContainerClient(containerName);
    }

    @Override
    public void setContainerClient(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    @Override
    public String getContainerName() {
        return this.containerClient.getBlobContainerName();
    }

    @Override
    public Response<?> createContainer(String containerName) {
        return client.createBlobContainerIfNotExistsWithResponse(containerName, null, null);
    }

    @Override
    public BlobContainerClient getCurrentContainerClient() {
        return this.containerClient;
    }   

    @Override
    public PagedIterable<BlobItem> getBlobs() {
        return this.containerClient.listBlobs();
    }

    @Override
    public Response<?> deleteContainer(String containerName) {
        return client.deleteBlobContainerWithResponse(containerName, null);
    }

    @Override
    public String uploadFile(MultipartFile file, String filename) {
        BlobClient blobClient  = this.containerClient.getBlobClient(filename);
        InputStream fileStream = null;
        try {
            fileStream = file.getInputStream();
        } catch (IOException ex) {
            return "error";
        }
        if (file.getSize() > 5242880) {
            return "too-large";
        }
        blobClient.upload(fileStream);
        return "success";
    }
}
