package com.electromart.backend.controller;

import com.electromart.backend.dto.CategoryDTO;
import com.electromart.backend.model.Category;
import com.electromart.backend.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }



    @PostMapping("/add")
    public Category addCategory(@RequestBody CategoryDTO categoryDTO) {

        Category category = new Category();

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        return categoryService.saveCategory(category);

    }

    @GetMapping("/all")
    public List<Category> getAllCategories() {

        return categoryService.getAllCategories();

    }



    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {

        return categoryService.getCategoryById(id);

    }

}