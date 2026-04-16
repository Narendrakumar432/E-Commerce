package com.ecommerce_backend.service;

import com.ecommerce_backend.dto.request.PaymentRequest;
import com.ecommerce_backend.dto.request.PaymentVerifyRequest;
import com.ecommerce_backend.dto.response.ApiResponse;

public interface PaymentService {

    // 🔹 Step 1: Create Razorpay Order
    ApiResponse createPaymentOrder(String email, PaymentRequest request);

    // 🔹 Step 2: Verify Payment after success
    ApiResponse verifyPayment(PaymentVerifyRequest request);
}