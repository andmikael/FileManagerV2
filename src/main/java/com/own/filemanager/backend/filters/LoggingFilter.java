package com.own.filemanager.backend.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.*;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        /*System.out.println("-------------- listing request headers -------------------");
        Collections.list(request.getHeaderNames()).forEach(header -> {
            log.info("Header: {}={}", header, request.getHeader(header));
        });
        System.out.println("-------------- listing response headers -------------------");
        Collection<String> head = response.getHeaderNames();
        @SuppressWarnings({ "rawtypes", "unchecked" })
        List<String> theList = new ArrayList(head);
        if (!theList.isEmpty()) {
            for (String e : theList) {
                log.info("Header: {}={}", e, response.getHeader(e));
             }
        }*/

        filterChain.doFilter(request, response);
    }

}