package com.ecommerce_backend.repository;

import com.ecommerce_backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Find specific item in cart by cart id and product id
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}