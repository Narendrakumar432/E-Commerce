package com.ecommerce_backend.service;

public interface EmailService {

    void sendWelcomeEmail(String toEmail, String customerName);

    void sendOrderConfirmationEmail(String toEmail, String customerName,
                                    Long orderId, Double totalAmount);

    void sendPaymentSuccessEmail(String toEmail, String customerName,
                                  Long orderId, Double amount);
}