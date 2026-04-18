package com.electromart.backend.service;

import com.electromart.backend.exception.BadRequestException;
import com.electromart.backend.exception.ResourceNotFoundException;
import com.electromart.backend.model.*;
import com.electromart.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        CartRepository cartRepository,
                        CartItemRepository cartItemRepository,
                        UserRepository userRepository,
                        EmailService emailService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Transactional
    public Order createOrderFromCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Cart cart = cartRepository.findByUser(user);
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        double total = 0;
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
            orderItems.add(orderItem);
        }

        order.setTotalAmount(total);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        cartItemRepository.deleteAll(cart.getCartItems());

        return savedOrder;
    }

    @Transactional
    public Order cancelOrder(Long orderId, Long userId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        // Security check — user can only cancel their own orders
        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException("You are not authorized to cancel this order");
        }

        // Only PAID or PROCESSING orders can be cancelled
        if (!canBeCancelled(order.getStatus())) {
            throw new BadRequestException("Order cannot be cancelled at this stage: " + order.getStatus());
        }

        order.setStatus("CANCELLED");
        order.setCancellationReason(reason);
        order.setCancellationDate(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // Send cancellation + refund email
        String email = order.getUser().getEmail();
        String name  = order.getUser().getName();
        emailService.sendCancellationEmail(email, name, orderId, order.getTotalAmount(), reason);

        return savedOrder;
    }

    private boolean canBeCancelled(String status) {
        return "PAID".equals(status) || "PROCESSING".equals(status) || "PENDING".equals(status);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}