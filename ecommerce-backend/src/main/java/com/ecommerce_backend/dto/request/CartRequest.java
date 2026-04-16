package com.ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// What user sends to add item to cart
// {
//   "productId": 1,
//   "quantity": 2
// }
public class CartRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;

    // Getters
    public Long getProductId() { return productId; }
    public Integer getQuantity() { return quantity; }

    // Setters
    public void setProductId(Long productId) { this.productId = productId; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}