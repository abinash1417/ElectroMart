package com.electromart.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentMethod;

    private String cardNumber;

    private String cardHolderName;

    private String expiryDate;

    private Double amount;

    private LocalDateTime paymentDate;

    private String status;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}