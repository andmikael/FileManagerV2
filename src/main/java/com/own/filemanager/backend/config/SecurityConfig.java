package com.own.filemanager.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.own.filemanager.backend.security.BlobAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final BlobAuthenticationFilter blobAuthenticationFilter;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(System.getenv("CORS_URL")));
        configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "UserRole"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
        .csrf(c -> c.disable())
        .cors((CorsConfigurer<HttpSecurity> c) -> c.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(r -> r.anyRequest().authenticated())
        //.formLogin(form -> form.loginPage("/api/auth/").permitAll())

        // -------------------------

        // IMPORTANT: FIX AUTHENTICATION BEING DONE TWICE AND SESSION NOT BEING CREATED

        // ----------------------------
        .addFilterAfter(blobAuthenticationFilter, BasicAuthenticationFilter.class);
        return http.build();
    }
}

        /*SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
        //storing the session
        http.securityContext((context) -> context.securityContextRepository(securityContextRepository));

        //session management
        http.sessionManagement((session) -> {
                session.maximumSessions(1).maxSessionsPreventsLogin(true);
                session.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::newSession);
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
             }
        
        );*/
