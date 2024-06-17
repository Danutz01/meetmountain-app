package com.meetmountain.meetmountainapp.service;

import com.meetmountain.meetmountainapp.entity.Product;
import com.meetmountain.meetmountainapp.dao.ProductRepository;
import com.meetmountain.meetmountainapp.dao.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public Product getProductById(Long id) {
        // Poate fi adăugat logging suplimentar aici pentru debugging
        Optional<Product> result = productRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RuntimeException("Product not found for id :: " + id);
        }
    }

    public Product saveProduct(Product product, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            product.setImage(imageFile.getBytes());
        }
        product.setCategoryToSecondChance(productCategoryRepository); // Setăm categoria la "a doua șansă"
        return productRepository.save(product);
    }

    public byte[] getProductImage(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(Product::getImage).orElse(null);
    }

    public List<Product> getAllProductsSortedByRating() {
        logger.info("Fetching all products sorted by rating");
        List<Product> products = productRepository.findAllByOrderByRatingDesc();
        logger.info("Fetched {} products", products.size());
        return products;
    }

    public Product updateProductRating(Long productId, int rating) {
        logger.info("Updating rating for product id: {}", productId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setRating(rating);
        return productRepository.save(product);
    }
}
