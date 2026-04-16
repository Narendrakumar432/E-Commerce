package com.ecommerce_backend.service;

import com.ecommerce_backend.dto.request.OrderRequest;
import com.ecommerce_backend.dto.response.OrderResponse;
import com.ecommerce_backend.enums.OrderStatus;
import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(String email, OrderRequest request);
    List<OrderResponse> getMyOrders(String email);
    OrderResponse getOrderById(String email, Long orderId);
    OrderResponse updateOrderStatus(Long orderId, OrderStatus status); // Admin
    List<OrderResponse> getAllOrders(); // Admin
}