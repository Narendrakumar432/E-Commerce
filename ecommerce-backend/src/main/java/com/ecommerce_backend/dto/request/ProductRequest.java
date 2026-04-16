package com.ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// What admin sends when creating/updating a product
// {
//   "name": "iPhone 15",
//   "description": "Latest iPhone",
//   "price": 79999.0,
//   "stock": 50,
//   "imageUrl": "http://...",
//   "categoryId": 1
// }
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Stock is required")
    @Positive(message = "Stock must be greater than 0")
    private Integer stock;

    private String imageUrl;

    @NotNull(message = "Category is required")
    private Long categoryId;

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public Integer getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }
    public Long getCategoryId() { return categoryId; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Double price) { this.price = price; }
    public void setStock(Integer stock) { this.stock = stock; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}