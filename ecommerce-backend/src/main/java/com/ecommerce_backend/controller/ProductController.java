package com.ecommerce_backend.controller;

import com.ecommerce_backend.dto.request.ProductRequest;
import com.ecommerce_backend.dto.response.ApiResponse;
import com.ecommerce_backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ADMIN ONLY - Create product
    // POST /api/admin/products
    @PostMapping("/admin/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Product created",
                productService.createProduct(request)));
    }

    // PUBLIC - Get all products
    // GET /api/products
    @GetMapping("/products")
    public ResponseEntity<ApiResponse> getAllProducts() {
        return ResponseEntity.ok(
            new ApiResponse(true, "Products fetched",
                productService.getAllProducts()));
    }

    // PUBLIC - Get product by ID
    // GET /api/products/1
    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Product fetched",
                productService.getProductById(id)));
    }

    // PUBLIC - Get products by category
    // GET /api/products/category/1
    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<ApiResponse> getProductsByCategory(
            @PathVariable Long categoryId) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Products fetched",
                productService.getProductsByCategory(categoryId)));
    }

    // PUBLIC - Search products by name
    // GET /api/products/search?keyword=iphone
    @GetMapping("/products/search")
    public ResponseEntity<ApiResponse> searchProducts(
            @RequestParam String keyword) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Search results",
                productService.searchProducts(keyword)));
    }

    // ADMIN ONLY - Update product
    // PUT /api/admin/products/1
    @PutMapping("/admin/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Product updated",
                productService.updateProduct(id, request)));
    }

    // ADMIN ONLY - Delete product (soft delete)
    // DELETE /api/admin/products/1
    @DeleteMapping("/admin/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse(true, "Product deleted"));
    }
}