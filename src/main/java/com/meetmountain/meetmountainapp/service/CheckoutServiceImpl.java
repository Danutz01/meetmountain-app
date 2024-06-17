package com.meetmountain.meetmountainapp.service;

import com.meetmountain.meetmountainapp.dao.CustomerRepository;
import com.meetmountain.meetmountainapp.dto.PaymentInfo;
import com.meetmountain.meetmountainapp.dto.Purchase;
import com.meetmountain.meetmountainapp.dto.PurchaseResponse;
import com.meetmountain.meetmountainapp.entity.Customer;
import com.meetmountain.meetmountainapp.entity.Order;
import com.meetmountain.meetmountainapp.entity.OrderItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private CustomerRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository, @Value("${stripe.key.secret}") String secretKey) {
        this.customerRepository = customerRepository;

        //initialize Stripe API with secret key
        Stripe.apiKey = secretKey;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        try {
            Order order = purchase.getOrder();
            System.out.println("Order: " + order); // Log pentru verificare

            String orderTrackingNumber = generateOrderTrackingNumber();
            order.setOrderTrackingNumber(orderTrackingNumber);

            Set<OrderItem> orderItems = purchase.getOrderItems();
            orderItems.forEach(order::add);

            order.setShippingAddress(purchase.getShippingAddress());
            order.setBillingAddress(purchase.getBillingAddress());

            Customer customer = purchase.getCustomer();
            System.out.println("Customer: " + customer); // Log pentru verificare

            String theEmail = customer.getEmail();
            Customer customerFromDataBase = customerRepository.findByEmail(theEmail);

            if (customerFromDataBase != null) {
                customer = customerFromDataBase;
            }

            customer.add(order);

            customerRepository.save(customer);

            return new PurchaseResponse(orderTrackingNumber);
        } catch (Exception e) {
            System.err.println("Error placing order: " + e.getMessage());
            throw e;
        }
    }


    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "Comanda de pe site-ul Meetmountain");
        params.put("receipt_email", paymentInfo.getReceiptEmail());

        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {
        //generate a random UUID number(universaly unique id)
        return UUID.randomUUID().toString();
    }
}

