package com.ecommerce_backend.controller;

import com.ecommerce_backend.dto.request.CategoryRequest;
import com.ecommerce_backend.dto.response.ApiResponse;
import com.ecommerce_backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Only ADMIN can create category
    // POST /api/admin/categories
    @PostMapping("/admin/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Category created",
                categoryService.createCategory(request)));
    }

    // Anyone logged in can see categories
    // GET /api/categories
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse> getAllCategories() {
        return ResponseEntity.ok(
            new ApiResponse(true, "Categories fetched",
                categoryService.getAllCategories()));
    }

    // GET /api/categories/{id}
    @GetMapping("/categories/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Category fetched",
                categoryService.getCategoryById(id)));
    }

    // Only ADMIN can update
    // PUT /api/admin/categories/{id}
    @PutMapping("/admin/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(
            new ApiResponse(true, "Category updated",
                categoryService.updateCategory(id, request)));
    }

    // Only ADMIN can delete
    // DELETE /api/admin/categories/{id}
    @DeleteMapping("/admin/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new ApiResponse(true, "Category deleted"));
    }
}