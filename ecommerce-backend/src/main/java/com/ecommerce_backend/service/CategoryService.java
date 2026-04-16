package com.ecommerce_backend.service;

import com.ecommerce_backend.dto.request.CategoryRequest;
import com.ecommerce_backend.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryRequest request);
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    Category updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
}