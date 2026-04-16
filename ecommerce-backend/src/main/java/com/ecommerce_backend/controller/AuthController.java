package com.ecommerce_backend.controller;

import com.ecommerce_backend.dto.request.LoginRequest;
import com.ecommerce_backend.dto.request.RegisterRequest;
import com.ecommerce_backend.dto.response.AuthResponse;
import com.ecommerce_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @RestController = this class handles REST API requests
// @RequestMapping = all URLs in this class start with /api/auth
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // POST /api/auth/register
    // @Valid triggers the validation annotations in RegisterRequest
    // @RequestBody reads the JSON body from the request
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}