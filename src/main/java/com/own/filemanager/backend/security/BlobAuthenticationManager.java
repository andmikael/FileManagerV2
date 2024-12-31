package com.own.filemanager.backend.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class BlobAuthenticationManager implements AuthenticationManager {
    private final BlobAuthenticationProvider blobAuthenticationProvider;

    public BlobAuthenticationManager(BlobAuthenticationProvider blobAuthenticationProvider) {
        this.blobAuthenticationProvider = blobAuthenticationProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return blobAuthenticationProvider.authenticate(authentication);
    }
    
}
