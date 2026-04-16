package com.ecommerce_backend.service.impl;

import com.ecommerce_backend.dto.request.CartRequest;
import com.ecommerce_backend.dto.response.CartResponse;
import com.ecommerce_backend.entity.Cart;
import com.ecommerce_backend.entity.CartItem;
import com.ecommerce_backend.entity.Product;
import com.ecommerce_backend.entity.User;
import com.ecommerce_backend.exception.ResourceNotFoundException;
import com.ecommerce_backend.repository.CartItemRepository;
import com.ecommerce_backend.repository.CartRepository;
import com.ecommerce_backend.repository.ProductRepository;
import com.ecommerce_backend.repository.UserRepository;
import com.ecommerce_backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // ============================
    // CREATE OR GET CART
    // ============================
    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    // ============================
    // BUILD RESPONSE DTO
    // ============================
    private CartResponse buildCartResponse(Cart cart) {
        List<CartResponse.CartItemResponse> items = new ArrayList<>();
        double total = 0;

        if (cart.getCartItems() != null) {
            for (CartItem item : cart.getCartItems()) {
                CartResponse.CartItemResponse dto =
                        new CartResponse.CartItemResponse(
                                item.getId(),
                                item.getProduct().getId(),
                                item.getProduct().getName(),
                                item.getProduct().getPrice(),
                                item.getQuantity()
                        );
                items.add(dto);
                total += item.getProduct().getPrice() * item.getQuantity();
            }
        }

        return new CartResponse(cart.getId(), items, total);
    }

    // ============================
    // ADD TO CART
    // ============================
    @Override
    public CartResponse addToCart(String email, CartRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock!");
        }

        Cart cart = getOrCreateCart(user);

        Optional<CartItem> existing =
                cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            cartItemRepository.save(item);
        }

        // 🔥 FIX: fetch with items
        Cart updatedCart = cartRepository.findByUserIdWithItems(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return buildCartResponse(updatedCart);
    }

    // ============================
    // GET CART
    // ============================
    @Override
    public CartResponse getCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserIdWithItems(user.getId())
                .orElseGet(() -> getOrCreateCart(user));

        return buildCartResponse(cart);
    }

    // ============================
    // UPDATE CART ITEM
    // ============================
    @Override
    public CartResponse updateCartItem(String email, Long cartItemId, Integer quantity) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!item.getCart().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        Cart cart = cartRepository.findByUserIdWithItems(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return buildCartResponse(cart);
    }

    // ============================
    // REMOVE ITEM
    // ============================
    @Override
    public void removeFromCart(String email, Long cartItemId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!item.getCart().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        cartItemRepository.delete(item);
    }

    // ============================
    // CLEAR CART
    // ============================
    @Override
    public void clearCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getCartItems() != null) {
            cart.getCartItems().clear();
            cartRepository.save(cart);
        }
    }
}