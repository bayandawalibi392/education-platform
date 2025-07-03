package com.education.course_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentRequiredException.class)
    public ResponseEntity<?> handlePaymentRequired(PaymentRequiredException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("فشل", ex.getMessage()));
    }

    record ErrorResponse(String error, String message) {}
}
