package com.ecommerce_backend.repository;

import com.ecommerce_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// JpaRepository gives us save(), findById(), findAll(), delete() etc. for FREE
// We just add custom methods we need
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA automatically writes the SQL for this!
    // SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // Check if email already exists (used during registration)
    boolean existsByEmail(String email);
}