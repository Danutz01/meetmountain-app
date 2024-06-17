package com.meetmountain.meetmountainapp.entity;

import com.meetmountain.meetmountainapp.dao.ProductCategoryRepository;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "product")
@Data // Automatically add getters and setters
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;

    @Column(name = "sku")
    private String sku;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "image_url")
    private String imageURL;

    @Column(name = "active")
    private boolean active;

    @Column(name = "units_in_stock")
    private int unitsInStock;

    @Column(name = "date_created")
    @CreationTimestamp
    private Date dateCreated;

    @Column(name = "last_updated")
    @UpdateTimestamp
    private Date lastUpdated;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "rating")
    private int rating;

    // Metodă pentru a seta categoria la "a doua șansă"
    public void setCategoryToSecondChance(ProductCategoryRepository productCategoryRepository) {
        ProductCategory secondChanceCategory = productCategoryRepository.findById(12L)
                .orElseThrow(() -> new IllegalArgumentException("Category 'a doua șansă' not found"));
        this.category = secondChanceCategory;
    }



    // Constructori
    public Product() {}

    public Product(String sku, String name, String description, BigDecimal unitPrice, String imageURL, boolean active, int unitsInStock, byte[] image, int rating) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.imageURL = imageURL;
        this.active = active;
        this.unitsInStock = unitsInStock;
        this.image = image;
        this.rating = rating;
    }
}
