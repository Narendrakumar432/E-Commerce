package com.ecommerce_backend.service;

import com.ecommerce_backend.dto.request.ProductRequest;
import com.ecommerce_backend.entity.Product;
import java.util.List;

public interface ProductService {
    Product createProduct(ProductRequest request);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    List<Product> getProductsByCategory(Long categoryId);
    List<Product> searchProducts(String keyword);
    Product updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
}