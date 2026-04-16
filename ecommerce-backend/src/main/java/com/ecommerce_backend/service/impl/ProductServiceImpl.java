package com.ecommerce_backend.service.impl;

import com.ecommerce_backend.dto.request.ProductRequest;
import com.ecommerce_backend.entity.Category;
import com.ecommerce_backend.entity.Product;
import com.ecommerce_backend.exception.ResourceNotFoundException;
import com.ecommerce_backend.repository.CategoryRepository;
import com.ecommerce_backend.repository.ProductRepository;
import com.ecommerce_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product createProduct(ProductRequest request) {

        // First check if category exists
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + request.getCategoryId()));

        // Create product object
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);  // link product to category

        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        // Return only active products
        return productRepository.findByIsActiveTrue();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + id));
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        // Check category exists first
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + categoryId));
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        // Search by product name
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Product updateProduct(Long id, ProductRequest request) {
        // Find existing product
        Product product = getProductById(id);

        // Find new category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + request.getCategoryId()));

        // Update fields
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        // Soft delete - just mark as inactive, don't actually delete from DB
        // This is better practice in real ecommerce
        product.setActive(false);
        productRepository.save(product);
    }
}