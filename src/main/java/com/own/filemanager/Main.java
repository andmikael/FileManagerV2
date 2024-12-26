package com.own.filemanager;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.own.filemanager.backend.service.BlobStorageService;
import com.own.filemanager.backend.service.FileStorageService;



@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan
public class Main {

    private static FileStorageService file;
    private static BlobStorageService blobService;

    public Main(FileStorageService file, BlobStorageService blobService) {
        Main.file = file;
        Main.blobService = blobService;
        
    }
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = SpringApplication.run(Main.class, args);
        file.init();
    }



    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);

        corsConfiguration.setAllowedOrigins(List.of(System.getenv("CORS_URL")));
        corsConfiguration.setAllowedHeaders(List.of("Origin", "Access-Control-Allow-Origin", "Content-Type",
            "Accept", "Authorization", "Origin, Accept", "X-Request-Headers"));
        corsConfiguration.setExposedHeaders(List.of("Origin", "Content-Type", "Accept", "Authorization",
            "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource urlBasedConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedConfigurationSource);
    }
};