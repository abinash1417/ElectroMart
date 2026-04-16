package com.electromart.backend.controller;

import com.electromart.backend.dto.ProductDTO;
import com.electromart.backend.model.Product;
import com.electromart.backend.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    // Add Product
    @PostMapping
    public Product addProduct(@RequestBody ProductDTO productDTO) {

        return productService.addProduct(productDTO);

    }


    // Get All Products
    @GetMapping
    public List<Product> getAllProducts() {

        return productService.getAllProducts();

    }


    // Get Product By ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {

        return productService.getProductById(id);

    }


    // Delete Product
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

    }

}