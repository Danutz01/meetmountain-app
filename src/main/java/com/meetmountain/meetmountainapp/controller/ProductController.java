package com.meetmountain.meetmountainapp.controller;

import com.meetmountain.meetmountainapp.entity.Product;
import com.meetmountain.meetmountainapp.service.ProductService;
import com.meetmountain.meetmountainapp.dao.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
//        try {
            System.out.println("Fetching product with id: " + id); // Debugging log
            Product product = productService.getProductById(id);
            if (product == null) {
                System.out.println("Product not found"); // Debugging log
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            System.out.println("Product found: " + product.getName()); // Debugging log
            return new ResponseEntity<>(product, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace(); // Log the stack trace
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Void> uploadProductImage(@PathVariable Long id, @RequestParam("image") MultipartFile imageFile) {
        try {
            Optional<Product> product = Optional.ofNullable(productService.getProductById(id));
            if (product.isPresent()) {
                productService.saveProduct(product.get(), imageFile);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        try {
            byte[] image = productService.getProductImage(id);
            if (image != null) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image.jpg\"")
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(image);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            // Log the exception (for debugging)
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/sorted")
    public List<Product> getProductsSortedByRating() {
        logger.info("Received request to fetch products sorted by rating");
        return productService.getAllProductsSortedByRating();
    }

    @PostMapping("/{id}/rating")
    public Product updateRating(@PathVariable Long id, @RequestParam int rating) {
        logger.info("Received request to update rating for product id: {}", id);
        return productService.updateProductRating(id, rating);
    }

    @PreAuthorize("hasAuthority('SCOPE_api.products.write')")
    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(
            @RequestParam("sku") String sku,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("unitPrice") BigDecimal unitPrice,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("unitsInStock") int unitsInStock) {

        logger.info("Received request to add product with SKU: {}", sku);

        try {
            Product product = new Product();
            product.setSku(sku);
            product.setName(name);
            product.setDescription(description);
            product.setUnitPrice(unitPrice);
            product.setUnitsInStock(unitsInStock);
            product.setActive(true); // Assuming the new product is active

            logger.info("Setting category to second chance");
            product.setCategoryToSecondChance(productCategoryRepository); // Set category to "a doua șansă"

            logger.info("Saving product: {}", product);
            Product savedProduct = productService.saveProduct(product, imageFile);

            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            logger.error("Error saving product: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
