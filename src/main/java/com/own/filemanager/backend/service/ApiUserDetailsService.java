package com.own.filemanager.backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.own.filemanager.backend.models.User;
import com.own.filemanager.backend.models.UserPrincipal;


@Service
public class ApiUserDetailsService implements UserDetailsService {
    private final BlobStorage blobStorage;

    public ApiUserDetailsService(BlobStorage blobStorage) {
        this.blobStorage = blobStorage;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        this.blobStorage.setConnString(username);
        if(!this.blobStorage.init()) {
            throw new UsernameNotFoundException("Could not find account with matching connection string"); 
        }
        return new UserPrincipal(new User(username));
    }
    
}
