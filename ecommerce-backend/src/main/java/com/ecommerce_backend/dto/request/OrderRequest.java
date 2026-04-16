package com.ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotNull;

// User sends this to place an order
// {
//   "addressId": 1
// }
// Cart items are automatically picked from user's cart
public class OrderRequest {

    @NotNull(message = "Address is required")
    private Long addressId;

    // Getter
    public Long getAddressId() { return addressId; }

    // Setter
    public void setAddressId(Long addressId) { this.addressId = addressId; }
}