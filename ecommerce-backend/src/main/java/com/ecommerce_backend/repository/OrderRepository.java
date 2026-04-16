package com.ecommerce_backend.repository;

import com.ecommerce_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Get all orders for a user, newest first
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}