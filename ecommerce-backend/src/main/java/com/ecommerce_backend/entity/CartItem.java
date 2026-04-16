package com.ecommerce_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // 🔥 ADD THIS
import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonIgnore   // 🔥 VERY IMPORTANT
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Getters
    public Long getId() { return id; }
    public Integer getQuantity() { return quantity; }
    public Cart getCart() { return cart; }
    public Product getProduct() { return product; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setCart(Cart cart) { this.cart = cart; }
    public void setProduct(Product product) { this.product = product; }
}