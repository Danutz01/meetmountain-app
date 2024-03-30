package com.meetmountain.meetmountainapp.dto;

import com.meetmountain.meetmountainapp.entity.Address;
import com.meetmountain.meetmountainapp.entity.Customer;
import com.meetmountain.meetmountainapp.entity.Order;
import com.meetmountain.meetmountainapp.entity.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}
