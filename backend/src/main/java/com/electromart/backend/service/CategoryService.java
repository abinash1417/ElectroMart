package com.electromart.backend.service;

import com.electromart.backend.dto.CategoryDTO;
import com.electromart.backend.model.Category;
import com.electromart.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) return null;
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}