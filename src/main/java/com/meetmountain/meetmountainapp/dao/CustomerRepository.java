package com.meetmountain.meetmountainapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.meetmountain.meetmountainapp.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
