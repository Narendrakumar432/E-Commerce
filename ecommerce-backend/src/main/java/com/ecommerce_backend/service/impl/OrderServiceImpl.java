package com.ecommerce_backend.service.impl;

import com.ecommerce_backend.dto.request.OrderRequest;
import com.ecommerce_backend.dto.response.OrderResponse;
import com.ecommerce_backend.entity.*;
import com.ecommerce_backend.enums.OrderStatus;
import com.ecommerce_backend.enums.PaymentStatus;
import com.ecommerce_backend.exception.ResourceNotFoundException;
import com.ecommerce_backend.repository.*;
import com.ecommerce_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // Helper - build OrderResponse from Order entity
    private OrderResponse buildOrderResponse(Order order) {
        List<OrderResponse.OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {
            itemResponses.add(new OrderResponse.OrderItemResponse(
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPriceAtPurchase()
            ));
        }

        // Build address string
        Address addr = order.getAddress();
        String addressStr = addr.getStreet() + ", " + addr.getCity()
                + ", " + addr.getState() + " - " + addr.getPincode();

        return new OrderResponse(
            order.getId(),
            order.getTotalAmount(),
            order.getStatus(),
            order.getCreatedAt(),
            addressStr,
            itemResponses
        );
    }

    @Override
    @Transactional
    // @Transactional = if anything fails, rollback everything
    // Example: if stock update fails, order is also rolled back
    public OrderResponse placeOrder(String email, OrderRequest request) {

        // Step 1: Get user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Step 2: Get address
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        // Step 3: Get user's cart
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart is empty!"));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty! Add items before placing order.");
        }

        // Step 4: Create order
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);

        // Step 5: Create order items from cart items
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();

            // Check stock again before placing order
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException(
                    "Insufficient stock for: " + product.getName());
            }

            // Reduce stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            // Store price at time of purchase (price may change later)
            orderItem.setPriceAtPurchase(product.getPrice());

            orderItems.add(orderItem);
            totalAmount += product.getPrice() * cartItem.getQuantity();
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        // Step 6: Save order
        Order savedOrder = orderRepository.save(order);

        // Step 7: Create pending payment record
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setAmount(totalAmount);
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);

        // Step 8: Clear the cart after order placed
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return buildOrderResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getMyOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            responses.add(buildOrderResponse(order));
        }
        return responses;
    }

    @Override
    public OrderResponse getOrderById(String email, Long orderId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + orderId));

        // Security check - order must belong to this user
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized!");
        }

        return buildOrderResponse(order);
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + orderId));
        order.setStatus(status);
        return buildOrderResponse(orderRepository.save(order));
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            responses.add(buildOrderResponse(order));
        }
        return responses;
    }
}