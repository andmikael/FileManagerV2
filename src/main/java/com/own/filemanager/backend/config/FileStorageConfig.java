package com.own.filemanager.backend.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.own.filemanager.backend.service.FileStorageService;
import com.own.filemanager.backend.service.StorageProperties;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class FileStorageConfig {
    
    @Bean
    @Primary
    public FileStorageService storageService(StorageProperties properties) {
        return new FileStorageService(properties);
    }
}
