package com.meetmountain.meetmountainapp.dao;

import com.meetmountain.meetmountainapp.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "ProductCategory", path = "product_category")
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
