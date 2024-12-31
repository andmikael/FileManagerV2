package com.own.filemanager.backend.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.own.filemanager.backend.models.UserPrincipal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Component
public class BlobAuthentication implements Authentication {
    private boolean authentication;
    private String connString;
    private String userRole;
    private UserPrincipal userPrincipal;
    public static int initiationTimes = 0;

    public BlobAuthentication() {
        
    }

    public BlobAuthentication(boolean authentication, String connString, String userRole) {
        BlobAuthentication.initiationTimes++;
        System.out.println("number of times Blobauthentication was initiated: " + initiationTimes);
        this.authentication = authentication;
        this.connString = connString;
        this.userRole = userRole;
        this.userPrincipal = null;
    }

    /*public BlobAuthentication(Boolean authentication, String key) {
        this.authentication = authentication;
        this.connString = key;
    }

    public void setauthentication(Boolean val) {
        this.authentication = val;
    }

    public void setconnString(String val) {
        this.connString = val;
    }

    public Boolean getauthentication() {
        return this.authentication;
    }

    public String getconnString() {
        return this.connString;
    }*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getCredentials() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getDetails() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getPrincipal() {
        return this.userPrincipal;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authentication;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authentication = isAuthenticated;
    }

    @Override
    public String getName() {
        return "";
    }

    public void SetPrincipal(UserPrincipal userPrincipal) {
        this.userPrincipal = userPrincipal;
    }
    
}
