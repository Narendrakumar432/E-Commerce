package com.ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotNull;

// User sends this to initiate payment
// {
//   "orderId": 1
// }
public class PaymentRequest {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    // Getter
    public Long getOrderId() { return orderId; }

    // Setter
    public void setOrderId(Long orderId) { this.orderId = orderId; }
}