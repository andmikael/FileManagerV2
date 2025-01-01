package com.own.filemanager.backend.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class BlobAuthenticationFilter extends OncePerRequestFilter {
    private final BlobAuthenticationManager blobAuthenticationManager;

    // Custom security filter that checks whether the request has the correct authorization header (custom beaerer token that is only sent once)
    // if token is correct, a new session ID will be created user is remembered until they log out
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader("Cookie") == null) {
            String headerKey = request.getHeader("Authorization");
            String userRole = request.getHeader("UserRole");
    
            String authKey = new String();
            if (headerKey != null) {
                String[] key = headerKey.split(" ");
                authKey = key[1].replaceAll("\\s+","");
            }
            String decodedString = new String(Base64.getDecoder().decode(authKey), StandardCharsets.UTF_8);
            BlobAuthentication blobAuthentication = new BlobAuthentication(false, decodedString, userRole);
            Authentication authObj = blobAuthenticationManager.authenticate(blobAuthentication);
            if (authObj.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authObj);
            }
        }
        filterChain.doFilter(request, response);
    }
    
}
