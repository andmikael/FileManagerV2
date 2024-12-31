package com.own.filemanager.backend.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class BlobAuthenticationFilter extends OncePerRequestFilter {
    private final BlobAuthenticationManager blobAuthenticationManager;

    public BlobAuthenticationFilter(BlobAuthenticationManager blobAuthenticationManager) {
        this.blobAuthenticationManager = blobAuthenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(request.getRequestURL());
        String headerKey = request.getHeader("Authorization");
        String userRole = request.getHeader("UserRole");

        Enumeration<String> e = request.getHeaderNames();
        for (String elem : Collections.list(e)) {
            System.out.println(elem);
        }

        System.out.println(headerKey);
        System.out.println(userRole);

        String authKey = new String();
        if (headerKey != null) {
            String[] key = headerKey.split(" ");
            authKey = key[1].replaceAll("\\s+","");
        }
        String decodedString = new String(Base64.getDecoder().decode(authKey), StandardCharsets.UTF_8);
        BlobAuthentication blobAuthentication = new BlobAuthentication(false, decodedString, userRole);
        Authentication authObj = blobAuthenticationManager.authenticate(blobAuthentication);
        SecurityContextHolder.getContext().setAuthentication(authObj);
        filterChain.doFilter(request, response);
    }
    
}
