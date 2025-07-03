package com.education.payment_service.controller;

import com.education.payment_service.model.Payment;
import com.education.payment_service.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

//    @PostMapping("/pay")
//    public ResponseEntity<Payment> pay(@RequestParam Long courseId,
//                                       @RequestParam double amount,
//                                       Authentication auth) {
//        String username = auth.getName();
//        Payment payment = service.processPayment(username, courseId, amount);
//        return ResponseEntity.ok(payment);
//    }
@PostMapping("/pay")
public ResponseEntity<Payment> pay(@RequestBody Payment payment, Authentication auth) {
    payment.setUsername(auth.getName());
    Payment saved = service.processPayment(payment.getUsername(), payment.getCourseId(), payment.getAmount());
    return ResponseEntity.ok(saved);
}

    @GetMapping("/my")
    public ResponseEntity<List<Payment>> getMyPayments(Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.ok(service.getPaymentsByUser(username));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkPayment(@RequestParam String username,
                                                @RequestParam Long courseId) {
        boolean paid = service.hasUserPaidForCourse(username, courseId);
        return ResponseEntity.ok(paid);
    }
}
