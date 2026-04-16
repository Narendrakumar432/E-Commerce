package com.ecommerce_backend.dto.response;

import java.util.List;

// What we send back when user views their cart
// {
//   "cartId": 1,
//   "items": [...],
//   "totalPrice": 1599.98
// }
public class CartResponse {

    private Long cartId;
    private List<CartItemResponse> items;
    private Double totalPrice;

    public CartResponse(Long cartId, List<CartItemResponse> items, Double totalPrice) {
        this.cartId = cartId;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    // Getters
    public Long getCartId() { return cartId; }
    public List<CartItemResponse> getItems() { return items; }
    public Double getTotalPrice() { return totalPrice; }

    // Setters
    public void setCartId(Long cartId) { this.cartId = cartId; }
    public void setItems(List<CartItemResponse> items) { this.items = items; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    // Inner class for each item in cart
    public static class CartItemResponse {
        private Long cartItemId;
        private Long productId;
        private String productName;
        private Double productPrice;
        private Integer quantity;
        private Double subtotal; // price x quantity

        public CartItemResponse(Long cartItemId, Long productId, String productName,
                                Double productPrice, Integer quantity) {
            this.cartItemId = cartItemId;
            this.productId = productId;
            this.productName = productName;
            this.productPrice = productPrice;
            this.quantity = quantity;
            this.subtotal = productPrice * quantity;
        }

        // Getters
        public Long getCartItemId() { return cartItemId; }
        public Long getProductId() { return productId; }
        public String getProductName() { return productName; }
        public Double getProductPrice() { return productPrice; }
        public Integer getQuantity() { return quantity; }
        public Double getSubtotal() { return subtotal; }
    }
}