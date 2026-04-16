package com.ecommerce_backend.service.impl;

import com.ecommerce_backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendWelcomeEmail(String toEmail, String customerName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Welcome to E-Commerce!");
            message.setText(
                "Dear " + customerName + ",\n\n" +
                "Welcome! Your account has been created successfully.\n\n" +
                "Start shopping now!\n\n" +
                "E-Commerce Team"
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Welcome email failed: " + e.getMessage());
        }
    }

    @Override
    public void sendOrderConfirmationEmail(String toEmail, String customerName,
                                            Long orderId, Double totalAmount) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Order Confirmed! #" + orderId);
            message.setText(
                "Dear " + customerName + ",\n\n" +
                "Your order has been placed successfully!\n\n" +
                "Order ID    : #" + orderId + "\n" +
                "Total Amount: Rs." + totalAmount + "\n\n" +
                "We will ship your order soon.\n\n" +
                "Thank you for shopping with us!\n" +
                "E-Commerce Team"
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Order email failed: " + e.getMessage());
        }
    }

    @Override
    public void sendPaymentSuccessEmail(String toEmail, String customerName,
                                         Long orderId, Double amount) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Payment Successful! Order #" + orderId);
            message.setText(
                "Dear " + customerName + ",\n\n" +
                "Payment received successfully!\n\n" +
                "Order ID  : #" + orderId + "\n" +
                "Amount Paid: Rs." + amount + "\n\n" +
                "Your order is confirmed and will be shipped soon.\n\n" +
                "E-Commerce Team"
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Payment email failed: " + e.getMessage());
        }
    }
}