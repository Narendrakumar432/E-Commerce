package com.ecommerce_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // 🔥 ADD THIS
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore   // 🔥 VERY IMPORTANT (fixes your error)
    private List<CartItem> cartItems;

    // Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public List<CartItem> getCartItems() { return cartItems; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setCartItems(List<CartItem> cartItems) { this.cartItems = cartItems; }
}