package com.kano.productAPI.service;

import com.kano.productAPI.model.Product;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private List<Product> products = new ArrayList<>();
    private Long nextId = 1L;

    @Value("${app.profile.description}")
    private String profileDescription;

    @Autowired(required = false)
    private ProductDataInitializer.DevDataInitializer devDataInitializer;

    @Autowired(required = false)
    private ProductDataInitializer.EmptyDataInitializer emptyDataInitializer;

    @PostConstruct
    public void init() {
        logger.info("Initializing ProductService with profile: {}", profileDescription);

        if (devDataInitializer != null) {
            products = new ArrayList<>(devDataInitializer.getInitialData());
            nextId = products.stream()
                    .mapToLong(Product::getId)
                    .max()
                    .orElse(0L) + 1;
            logger.info("Initialized with {} products", products.size());
        } else if (emptyDataInitializer != null) {
            products = emptyDataInitializer.getInitialData();
            logger.info("Initialized with empty product list");
        }
    }

    public List<Product> getAllProducts() {
        logger.debug("Getting all products. Current size: {}", products.size());
        return products;
    }

    public Optional<Product> getProductById(Long id) {
        logger.debug("Searching for product with id: {}", id);
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    public Product addProduct(Product product) {
        product.setId(nextId++);
        logger.info("Adding new product: {}", product);
        products.add(product);
        return product;
    }

    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        logger.info("Attempting to update product with id: {}", id);
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setPrice(updatedProduct.getPrice());
                    product.setStock(updatedProduct.getStock());
                    return product;
                });
    }

    public boolean deleteProduct(Long id) {
        logger.warn("Attempting to delete product with id: {}", id);
        return products.removeIf(product -> product.getId().equals(id));
    }
}