package com.meetmountain.meetmountainapp.controller;

import com.meetmountain.meetmountainapp.dto.PaymentInfo;
import com.meetmountain.meetmountainapp.dto.Purchase;
import com.meetmountain.meetmountainapp.dto.PurchaseResponse;
import com.meetmountain.meetmountainapp.service.CheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.logging.Logger;


@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private Logger logger = Logger.getLogger(getClass().getName());

    private final CheckoutService checkoutService;

    @PostMapping("/purchase")
    public PurchaseResponse placeOrder(@RequestBody Purchase purchase) {
        try {
            return checkoutService.placeOrder(purchase);
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error placing order", e);
        }
    }

    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfo paymentInfo) {
        try {
            logger.info("paymentInfo.amount: " + paymentInfo.getAmount());

            PaymentIntent paymentIntent = checkoutService.createPaymentIntent(paymentInfo);

            String paymentStr = paymentIntent.toJson();

            return new ResponseEntity<>(paymentStr, HttpStatus.OK);
        } catch (StripeException e) {
            logger.severe("Error creating payment intent: " + e.getMessage());
            return new ResponseEntity<>("Error creating payment intent: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

