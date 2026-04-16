package com.ecommerce_backend.controller;

import com.ecommerce_backend.dto.request.AddressRequest;
import com.ecommerce_backend.dto.response.ApiResponse;
import com.ecommerce_backend.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // POST /api/addresses
    @PostMapping
    public ResponseEntity<ApiResponse> addAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Address added",
                addressService.addAddress(userDetails.getUsername(), request)));
    }

    // GET /api/addresses
    @GetMapping
    public ResponseEntity<ApiResponse> getMyAddresses(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Addresses fetched",
                addressService.getMyAddresses(userDetails.getUsername())));
    }

    // PUT /api/addresses/1
    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse> updateAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Address updated",
                addressService.updateAddress(
                    userDetails.getUsername(), addressId, request)));
    }

    // DELETE /api/addresses/1
    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse> deleteAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long addressId) {
        addressService.deleteAddress(userDetails.getUsername(), addressId);
        return ResponseEntity.ok(new ApiResponse(true, "Address deleted"));
    }
}