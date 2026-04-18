package com.electromart.backend.controller;

import com.electromart.backend.dto.PaymentDTO;
import com.electromart.backend.model.Payment;
import com.electromart.backend.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/make")
    public Payment makePayment(@RequestBody PaymentDTO paymentDTO) {

        return paymentService.makePayment(
                paymentDTO.getOrderId(),
                paymentDTO.getCardNumber(),
                paymentDTO.getPaymentMethod(),
                paymentDTO.getAmount()
        );
    }
}