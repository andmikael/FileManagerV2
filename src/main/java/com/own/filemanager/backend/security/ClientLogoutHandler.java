package com.own.filemanager.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.own.filemanager.backend.service.BlobStorage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
public class ClientLogoutHandler implements LogoutHandler{

        private final BlobStorage blobStorage;

    public ClientLogoutHandler(BlobStorage blobStorage) {
        this.blobStorage = blobStorage;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        this.blobStorage.logout();
    }
    
}
