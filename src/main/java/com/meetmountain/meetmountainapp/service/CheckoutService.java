package com.meetmountain.meetmountainapp.service;

import com.meetmountain.meetmountainapp.dto.PaymentInfo;
import com.meetmountain.meetmountainapp.dto.Purchase;
import com.meetmountain.meetmountainapp.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
