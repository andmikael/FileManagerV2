package com.own.filemanager.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Basic;
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
        configuration.setAllowedOrigins(List.of("https://localhost:4220"));
        //configuration.setAllowedOrigins(List.of(System.getenv("CORS_URL")));
        configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "UserRole", "Access-Control-Allow-Credentials"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // custom security filter rules for authentication
    // uses custom made OncePerRequest filter for authorization
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /*http
        .csrf(c -> c.disable())
        .cors((CorsConfigurer<HttpSecurity> c) -> c.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(r -> r
        .requestMatchers("/api/auth/user", "api/auth/","api/auth/**", "/", "index.html").permitAll()
        .anyRequest().authenticated())
        //.formLogin(formLogin -> formLogin.loginPage("/api/auth/").permitAll())
        .securityContext((securityContext) -> securityContext.requireExplicitSave(true))
        .sessionManagement(c->c.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .addFilterBefore(blobAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();*/

        return http.csrf(c -> c.disable())
        .cors(c -> c.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**", "/", "/index.html").permitAll()
        .anyRequest().authenticated())
        .formLogin(form -> form.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        )
        .addFilterBefore(blobAuthenticationFilter, BasicAuthenticationFilter.class)
        .securityContext(context -> context.securityContextRepository(new DelegatingSecurityContextRepository(
            new RequestAttributeSecurityContextRepository(),
            new HttpSessionSecurityContextRepository())))
        .build();
    }
}
