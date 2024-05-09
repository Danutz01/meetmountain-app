package com.meetmountain.meetmountainapp.service;

import com.meetmountain.meetmountainapp.dao.CustomerRepository;
import com.meetmountain.meetmountainapp.dto.Purchase;
import com.meetmountain.meetmountainapp.dto.PurchaseResponse;
import com.meetmountain.meetmountainapp.entity.Customer;
import com.meetmountain.meetmountainapp.entity.Order;
import com.meetmountain.meetmountainapp.entity.OrderItem;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private CustomerRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    //retrieve the order info from dto
    //generate tracking number
    //populate order with orderItems
    //populate order with billingAddress and shippingAddress
    //populate customer with order
    //save to the db
    //return a response
    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        Order order = purchase.getOrder();

        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        order.setShippingAddress(purchase.getShippingAddress());
        order.setBillingAddress(purchase.getBillingAddress());

        //populate customer with order
        Customer customer = purchase.getCustomer();

        //check if this is an existing custimer
        String theEmail = customer.getEmail();
        Customer customerFromDataBase = customerRepository.findByEmail(theEmail);

        if(customerFromDataBase != null){
            //we found them and assign them accordingly
            customer = customerFromDataBase;
        }

        //save to the database
        customer.add(order);

        //return a response
        customerRepository.save(customer);

        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {

        //generate a random UUID number(universaly unique id)
        return UUID.randomUUID().toString();
    }
}
