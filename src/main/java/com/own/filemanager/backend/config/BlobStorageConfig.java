package com.own.filemanager.backend.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.own.filemanager.backend.service.BlobProperties;
import com.own.filemanager.backend.service.BlobStorageService;

@Configuration
@EnableConfigurationProperties(BlobProperties.class)
public class BlobStorageConfig {
    
    @Bean
    @Primary
    public BlobStorageService blobService(BlobProperties properties) {
        return new BlobStorageService(properties);
    }
}
