package com.ecommerce_backend.dto.response;

import com.ecommerce_backend.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long orderId;
    private Double totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private String deliveryAddress;
    private List<OrderItemResponse> items;

    public OrderResponse(Long orderId, Double totalAmount, OrderStatus status,
                         LocalDateTime createdAt, String deliveryAddress,
                         List<OrderItemResponse> items) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.deliveryAddress = deliveryAddress;
        this.items = items;
    }

    // Getters
    public Long getOrderId() { return orderId; }
    public Double getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public List<OrderItemResponse> getItems() { return items; }

    // Inner class for order items
    public static class OrderItemResponse {
        private String productName;
        private Integer quantity;
        private Double priceAtPurchase;
        private Double subtotal;

        public OrderItemResponse(String productName, Integer quantity,
                                  Double priceAtPurchase) {
            this.productName = productName;
            this.quantity = quantity;
            this.priceAtPurchase = priceAtPurchase;
            this.subtotal = priceAtPurchase * quantity;
        }

        // Getters
        public String getProductName() { return productName; }
        public Integer getQuantity() { return quantity; }
        public Double getPriceAtPurchase() { return priceAtPurchase; }
        public Double getSubtotal() { return subtotal; }
    }
}