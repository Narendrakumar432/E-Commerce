package com.ecommerce_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // 🔥 ADD THIS
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private String imageUrl;

    private boolean isActive = true;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore   // 🔥 VERY IMPORTANT (FIXES YOUR ERROR)
    private List<Product> products;

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public boolean isActive() { return isActive; }
    public List<Product> getProducts() { return products; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setActive(boolean isActive) { this.isActive = isActive; }
    public void setProducts(List<Product> products) { this.products = products; }
}