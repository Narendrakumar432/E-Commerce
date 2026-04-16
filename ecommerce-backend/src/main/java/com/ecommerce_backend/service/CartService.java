package com.ecommerce_backend.service;

import com.ecommerce_backend.dto.request.CartRequest;
import com.ecommerce_backend.dto.response.CartResponse;

public interface CartService {

    // Add item to cart
    CartResponse addToCart(String email, CartRequest request);

    // Get full cart
    CartResponse getCart(String email);

    // Update quantity of item
    CartResponse updateCartItem(String email, Long cartItemId, Integer quantity);

    // Remove single item
    void removeFromCart(String email, Long cartItemId);

    // Clear entire cart
    void clearCart(String email);
}