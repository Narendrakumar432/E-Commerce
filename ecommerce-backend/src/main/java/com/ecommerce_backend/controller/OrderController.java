package com.ecommerce_backend.controller;

import com.ecommerce_backend.dto.request.OrderRequest;
import com.ecommerce_backend.dto.response.ApiResponse;
import com.ecommerce_backend.enums.OrderStatus;
import com.ecommerce_backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // USER - Place order from cart
    // POST /api/orders
    @PostMapping("/orders")
    public ResponseEntity<ApiResponse> placeOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Order placed successfully",
                orderService.placeOrder(userDetails.getUsername(), request)));
    }

    // USER - Get my orders
    // GET /api/orders/my
    @GetMapping("/orders/my")
    public ResponseEntity<ApiResponse> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Orders fetched",
                orderService.getMyOrders(userDetails.getUsername())));
    }

    // USER - Get specific order
    // GET /api/orders/1
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Order fetched",
                orderService.getOrderById(userDetails.getUsername(), orderId)));
    }

    // ADMIN - Get all orders
    // GET /api/admin/orders
    @GetMapping("/admin/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllOrders() {
        return ResponseEntity.ok(
            new ApiResponse(true, "All orders fetched",
                orderService.getAllOrders()));
    }

    // ADMIN - Update order status
    // PUT /api/admin/orders/1/status?status=SHIPPED
    @PutMapping("/admin/orders/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Order status updated",
                orderService.updateOrderStatus(orderId, status)));
    }
}