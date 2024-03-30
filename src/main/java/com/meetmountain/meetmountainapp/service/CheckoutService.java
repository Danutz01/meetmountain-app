package com.meetmountain.meetmountainapp.service;

import com.meetmountain.meetmountainapp.dto.Purchase;
import com.meetmountain.meetmountainapp.dto.PurchaseResponse;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);
}
