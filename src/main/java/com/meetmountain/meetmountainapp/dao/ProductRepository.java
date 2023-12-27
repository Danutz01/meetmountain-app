package com.meetmountain.meetmountainapp.dao;

import com.meetmountain.meetmountainapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
