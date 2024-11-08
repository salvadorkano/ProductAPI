package com.kano.productAPI.controller;

import com.kano.productAPI.model.Product;
import com.kano.productAPI.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllProducts_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void getAllProducts_WhenNotEmpty_ShouldReturnList() throws Exception {
        Product product = new Product(1L, "Test Product", 99.99, 10);
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Product"))
                .andExpect(jsonPath("$[0].price").value(99.99))
                .andExpect(jsonPath("$[0].stock").value(10));
    }

    @Test
    public void getProductById_WhenExists_ShouldReturnProduct() throws Exception {
        Product product = new Product(1L, "Test Product", 99.99, 10);
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    public void getProductById_WhenNotExists_ShouldReturn404() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createProduct_ShouldReturnCreatedProduct() throws Exception {
        Product productToCreate = new Product(null, "New Product", 149.99, 5);
        Product createdProduct = new Product(1L, "New Product", 149.99, 5);

        when(productService.addProduct(any(Product.class))).thenReturn(createdProduct);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Product"));
    }

    @Test
    public void updateProduct_WhenExists_ShouldReturnUpdatedProduct() throws Exception {
        Product productToUpdate = new Product(1L, "Updated Product", 199.99, 15);

        when(productService.updateProduct(eq(1L), any(Product.class)))
                .thenReturn(Optional.of(productToUpdate));

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    public void deleteProduct_WhenExists_ShouldReturn200() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteProduct_WhenNotExists_ShouldReturn404() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNotFound());
    }
}