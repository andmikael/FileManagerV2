package com.own.filemanager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import com.own.filemanager.backend.service.BlobStorageService;



@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan
public class Main {

    private BlobStorageService blobService;

    public Main(BlobStorageService blobService) {
        this.blobService = blobService;
        
    }
    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        /*String[] beans = context.getBeanFactory().getBeanDefinitionNames();
        for (String elem : beans) {
            System.out.println(elem);
        }*/
    }

    @Bean
    SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
};