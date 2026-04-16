package com.ecommerce_backend.repository;

import com.ecommerce_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find all products by category
    // SELECT * FROM products WHERE category_id = ?
    List<Product> findByCategoryId(Long categoryId);

    // Search products by name (case insensitive)
    // SELECT * FROM products WHERE name LIKE %keyword%
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Find only active products
    List<Product> findByIsActiveTrue();
}