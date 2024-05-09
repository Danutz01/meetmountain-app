package com.meetmountain.meetmountainapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.meetmountain.meetmountainapp.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String theEmail);
    //SELECT * FROM Customer c WHERE c.email = theEmail
}
