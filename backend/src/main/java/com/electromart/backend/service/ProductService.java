package com.electromart.backend.service;

import com.electromart.backend.dto.ProductDTO;
import com.electromart.backend.model.Category;
import com.electromart.backend.model.Product;
import com.electromart.backend.repository.CategoryRepository;
import com.electromart.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    public Product addProduct(ProductDTO productDTO) {

        Category category =
                categoryRepository.findById(productDTO.getCategoryId())
                        .orElse(null);

        Product product = new Product();

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setFrontImage(productDTO.getFrontImage());
        product.setCategory(category);

        return productRepository.save(product);
    }


    // Get All Products
    public List<Product> getAllProducts() {

        return productRepository.findAll();

    }


    // Get Product By ID
    public Product getProductById(Long id) {

        return productRepository.findById(id).orElse(null);

    }


    // Delete Product
    public void deleteProduct(Long id) {

        productRepository.deleteById(id);

    }

    public Product updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return null;

        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElse(null);

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setFrontImage(productDTO.getFrontImage());
        product.setCategory(category);

        return productRepository.save(product);
    }


}