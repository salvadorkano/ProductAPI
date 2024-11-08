package com.kano.productAPI.service;

import com.kano.productAPI.model.Product;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ProductDataInitializer {
    @Profile("dev")
    @Component
    public static class DevDataInitializer {
        public List<Product> getInitialData() {
            return Arrays.asList(
                    new Product(1L, "Laptop Gaming", 1299.99, 5),
                    new Product(2L, "Smartphone", 699.99, 10),
                    new Product(3L, "Smart Watch", 299.99, 15)
            );
        }
    }

    @Profile("empty")
    @Component
    public static class EmptyDataInitializer {
        public List<Product> getInitialData() {
            return new ArrayList<>();
        }
    }
}
