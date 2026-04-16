package com.ecommerce_backend.service.impl;

import com.ecommerce_backend.dto.request.LoginRequest;
import com.ecommerce_backend.dto.request.RegisterRequest;
import com.ecommerce_backend.dto.response.AuthResponse;
import com.ecommerce_backend.entity.User;
import com.ecommerce_backend.enums.Role;
import com.ecommerce_backend.repository.UserRepository;
import com.ecommerce_backend.security.JwtUtil;
import com.ecommerce_backend.service.AuthService;
import com.ecommerce_backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // EmailService injected here
    @Autowired
    private EmailService emailService;

    @Override
    public AuthResponse register(RegisterRequest request) {

        // Step 1: Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        // Step 2: Create new User object
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Step 3: Hash password — never store plain text
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());

        // Step 4: Default role is USER
        user.setRole(Role.USER);

        // Step 5: Save to database
        userRepository.save(user);

        // Step 6: Send welcome email
        // Wrapped in try-catch so registration doesn't fail if email fails
        emailService.sendWelcomeEmail(user.getEmail(), user.getName());

        // Step 7: Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        // Step 8: Return response
        return new AuthResponse(
                token,
                user.getEmail(),
                user.getName(),
                user.getRole().name()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        // Step 1: Verify email and password
        // Throws exception automatically if wrong credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Step 2: Load user from database
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Step 3: Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        // Step 4: Return response
        return new AuthResponse(
                token,
                user.getEmail(),
                user.getName(),
                user.getRole().name()
        );
    }
}