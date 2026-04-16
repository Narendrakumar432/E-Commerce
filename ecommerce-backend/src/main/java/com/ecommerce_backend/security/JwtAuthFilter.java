package com.ecommerce_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// This filter runs on EVERY request before it reaches the controller
// It checks: does this request have a valid JWT token?
// OncePerRequestFilter = runs exactly once per request
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1: Get the Authorization header from request
        // It looks like: "Bearer eyJhbGciOiJIUzI1NiJ9..."
        String authHeader = request.getHeader("Authorization");

        String token = null;
        String email = null;

        // Step 2: Check if header exists and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extract just the token part (remove "Bearer " prefix)
            token = authHeader.substring(7);

            // Extract email from token
            email = jwtUtil.extractEmail(token);
        }

        // Step 3: If we got an email and user is not already authenticated
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user details from database
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Step 4: Validate the token
            if (jwtUtil.isTokenValid(token)) {

                // Step 5: Create authentication object
                // This tells Spring Security: this user is authenticated
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities() // ROLE_USER or ROLE_ADMIN
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Step 6: Set authentication in Security Context
                // Now Spring Security knows who is making this request
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Step 7: Continue to next filter / controller
        filterChain.doFilter(request, response);
    }
}