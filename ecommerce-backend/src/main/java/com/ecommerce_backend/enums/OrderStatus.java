package com.ecommerce_backend.enums;

public enum OrderStatus {
    PENDING,      // order placed, payment not done
    CONFIRMED,    // payment done, order confirmed
    SHIPPED,      // order dispatched
    DELIVERED,    // order received by customer
    CANCELLED     // order cancelled
}