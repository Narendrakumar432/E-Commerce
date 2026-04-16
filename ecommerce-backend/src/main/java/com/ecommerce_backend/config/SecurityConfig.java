package com.ecommerce_backend.config;

import com.ecommerce_backend.security.JwtAuthFilter;
import com.ecommerce_backend.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration = this class contains Spring configuration
// @EnableWebSecurity = enables Spring Security
// @EnableMethodSecurity = allows @PreAuthorize on controller methods
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Defines which URLs are public and which need authentication
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF - not needed for REST APIs (CSRF is for browser forms)
            .csrf(csrf -> csrf.disable())

            // Define URL access rules
            .authorizeHttpRequests(auth -> auth
                // These URLs are PUBLIC - no token needed
                .requestMatchers("/api/auth/**").permitAll()

                // Only ADMIN can access these
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // All other URLs need authentication (valid JWT token)
                .anyRequest().authenticated()
            )

            // Use STATELESS sessions - no session stored on server
            // Every request must send JWT token
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Tell Spring Security to use our UserDetailsService
            .authenticationProvider(authenticationProvider())

            // Add our JWT filter BEFORE Spring's default login filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // BCrypt is a strong password hashing algorithm
    // Passwords are NEVER stored as plain text in database
    // Example: "password123" → "$2a$10$xyz..." (hashed)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Connects our UserDetailsService + PasswordEncoder to Spring Security
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // AuthenticationManager is used to verify email + password during login
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}