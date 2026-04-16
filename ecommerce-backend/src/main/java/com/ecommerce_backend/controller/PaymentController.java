package com.ecommerce_backend.controller;

import com.ecommerce_backend.dto.request.PaymentRequest;
import com.ecommerce_backend.dto.request.PaymentVerifyRequest;
import com.ecommerce_backend.dto.response.ApiResponse;
import com.ecommerce_backend.service.PaymentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")   // 🔥 plural (REST standard)
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // ================================
    // CREATE PAYMENT ORDER
    // ================================
    // POST /api/payments/create
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createPaymentOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PaymentRequest request) {

        String email = userDetails.getUsername(); // logged-in user email

        return ResponseEntity.ok(
                paymentService.createPaymentOrder(email, request)
        );
    }

    // ================================
    // VERIFY PAYMENT
    // ================================
    // POST /api/payments/verify
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse> verifyPayment(
            @Valid @RequestBody PaymentVerifyRequest request) {

        return ResponseEntity.ok(
                paymentService.verifyPayment(request)
        );
    }
}