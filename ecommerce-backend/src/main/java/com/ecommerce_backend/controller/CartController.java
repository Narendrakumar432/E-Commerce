package com.ecommerce_backend.controller;

import com.ecommerce_backend.dto.request.CartRequest;
import com.ecommerce_backend.dto.response.ApiResponse;
import com.ecommerce_backend.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // @AuthenticationPrincipal gives us the currently logged in user
    // We extract their email from it

    // POST /api/cart/add
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CartRequest request) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Item added to cart",
                cartService.addToCart(userDetails.getUsername(), request)));
    }

    // GET /api/cart
    @GetMapping
    public ResponseEntity<ApiResponse> getCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Cart fetched",
                cartService.getCart(userDetails.getUsername())));
    }

    // PUT /api/cart/update/{cartItemId}?quantity=3
    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<ApiResponse> updateCartItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Cart updated",
                cartService.updateCartItem(
                    userDetails.getUsername(), cartItemId, quantity)));
    }

    // DELETE /api/cart/remove/{cartItemId}
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<ApiResponse> removeFromCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long cartItemId) {
        cartService.removeFromCart(userDetails.getUsername(), cartItemId);
        return ResponseEntity.ok(new ApiResponse(true, "Item removed from cart"));
    }

    // DELETE /api/cart/clear
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse> clearCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        cartService.clearCart(userDetails.getUsername());
        return ResponseEntity.ok(new ApiResponse(true, "Cart cleared"));
    }
}