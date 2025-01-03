package com.own.filemanager.backend.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.own.filemanager.backend.models.User;
import com.own.filemanager.backend.models.UserPrincipal;
import com.own.filemanager.backend.service.BlobStorage;

@Component
public class BlobAuthenticationProvider implements AuthenticationProvider {
    private final BlobStorage blobStorage;

    public BlobAuthenticationProvider(BlobStorage blobStorage) {
        this.blobStorage = blobStorage;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            BlobAuthentication blobAuthentication = (BlobAuthentication) authentication;
            String headerKey = blobAuthentication.getConnString();
            String userRole = blobAuthentication.getUserRole();
    
            if (!this.blobStorage.init(headerKey) || headerKey == null) {
                throw new BadCredentialsException("auth error: wrong conn string");
            }
    
            blobAuthentication.setAuthenticated(true);
            blobAuthentication.setUserPrincipal(new UserPrincipal(new User(userRole)));
            return blobAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BlobAuthentication.class.equals(authentication);
    }
}
