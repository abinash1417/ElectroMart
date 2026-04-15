package com.electromart.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime orderDate;
    private Double totalAmount;

    // Status values: PENDING, PAID, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUND_PENDING, REFUNDED
    private String status;

    private String cancellationReason;
    private LocalDateTime cancellationDate;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems;

    @JsonIgnore
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;
}