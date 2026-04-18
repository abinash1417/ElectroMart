package com.electromart.backend.service;

import com.electromart.backend.model.Order;
import com.electromart.backend.model.Payment;
import com.electromart.backend.repository.OrderRepository;
import com.electromart.backend.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final EmailService emailService;

    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository,
                          EmailService emailService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.emailService = emailService;
    }

    public Payment makePayment(Long orderId, String cardNumber, String paymentMethod, double amount) {

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return null;

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setCardNumber(cardNumber);
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(amount);
        payment.setStatus("SUCCESS");

        Payment savedPayment = paymentRepository.save(payment);

        order.setStatus("PAID");
        orderRepository.save(order);

        // ✅ Send confirmation email after successful payment
        String customerEmail = order.getUser().getEmail();
        String customerName  = order.getUser().getName();
        Long   ordId         = order.getId();
        Double totalAmount   = order.getTotalAmount();

        System.out.println("=== Sending email to: " + customerEmail);
        System.out.println("=== Customer name: " + customerName);
        System.out.println("=== Order ID: " + ordId);
        System.out.println("=== Total Amount: " + totalAmount);
        emailService.sendOrderConfirmationEmail(customerEmail, customerName, ordId, totalAmount);
        System.out.println("=== Email sent successfully!");
        return savedPayment;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Payment updatePaymentStatus(Long paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment != null) {
            payment.setStatus(status);
            return paymentRepository.save(payment);
        }
        return null;
    }
}