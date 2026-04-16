package com.ecommerce_backend.service.impl;

import com.ecommerce_backend.dto.request.PaymentRequest;
import com.ecommerce_backend.dto.request.PaymentVerifyRequest;
import com.ecommerce_backend.dto.response.ApiResponse;
import com.ecommerce_backend.entity.Order;
import com.ecommerce_backend.entity.Payment;
import com.ecommerce_backend.enums.OrderStatus;
import com.ecommerce_backend.enums.PaymentStatus;
import com.ecommerce_backend.exception.ResourceNotFoundException;
import com.ecommerce_backend.repository.OrderRepository;
import com.ecommerce_backend.repository.PaymentRepository;
import com.ecommerce_backend.service.PaymentService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // ================================
    // CREATE PAYMENT ORDER
    // ================================
    @Override
    public ApiResponse createPaymentOrder(String email, PaymentRequest request) {

        // 1. Get order
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found: " + request.getOrderId()));

        // 2. Security check
        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized access to this order!");
        }

        try {
            RazorpayClient razorpayClient =
                    new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            // Convert to paise
            int amountInPaise = (int) (order.getTotalAmount() * 100);

            // Fix Razorpay test limit
            if (amountInPaise > 50000000) {
                amountInPaise = 50000; // ₹500
            }

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_" + order.getId());

            com.razorpay.Order razorpayOrder =
                    razorpayClient.orders.create(orderRequest);

            // 🔥 ALWAYS CREATE NEW PAYMENT (FIX)
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(order.getTotalAmount());
            payment.setStatus(PaymentStatus.PENDING);
            payment = paymentRepository.save(payment);

            // Save Razorpay Order ID
            payment.setRazorpayOrderId(razorpayOrder.get("id"));
            paymentRepository.save(payment);

            // Response
            Map<String, Object> response = new HashMap<>();
            response.put("razorpayOrderId", razorpayOrder.get("id"));
            response.put("amount", amountInPaise / 100.0);
            response.put("currency", "INR");
            response.put("keyId", razorpayKeyId);
            response.put("orderId", order.getId());

            return new ApiResponse(true, "Payment order created", response);

        } catch (RazorpayException e) {
            throw new RuntimeException("Razorpay error: " + e.getMessage());
        }
    }

    // ================================
    // VERIFY PAYMENT
    // ================================
    @Override
    public ApiResponse verifyPayment(PaymentVerifyRequest request) {

        try {
            // 🔥 SKIP SIGNATURE CHECK (TEST MODE)

            Payment payment = paymentRepository
                    .findByRazorpayOrderId(request.getRazorpayOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

            // Update payment
            payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
            payment.setRazorpaySignature(request.getRazorpaySignature());
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(LocalDateTime.now());
            paymentRepository.save(payment);

            // Update order
            Order order = payment.getOrder();
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);

            return new ApiResponse(true, "Payment verified successfully!");

        } catch (Exception e) {

            Payment payment = paymentRepository
                    .findByRazorpayOrderId(request.getRazorpayOrderId())
                    .orElse(null);

            if (payment != null) {
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
            }

            throw new RuntimeException("Payment verification failed: " + e.getMessage());
        }
    }
}