package com.ecommerce_backend.service;

import com.ecommerce_backend.dto.request.LoginRequest;
import com.ecommerce_backend.dto.request.RegisterRequest;
import com.ecommerce_backend.dto.response.AuthResponse;

// Interface defines WHAT to do
// Actual logic is written in AuthServiceImpl
public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}