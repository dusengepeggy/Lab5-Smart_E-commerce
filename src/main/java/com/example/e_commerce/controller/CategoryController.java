package com.example.e_commerce.controller;

import com.example.e_commerce.dto.JsonResponseDto.SuccessResponse;
import com.example.e_commerce.dto.RequestDto.UpdateCategoryRequest;
import com.example.e_commerce.model.Category;
import com.example.e_commerce.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
@Tag(name = "Categories", description = "Category management")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity createCategory(
            @RequestBody UpdateCategoryRequest request
    ) {
        categoryService.createCategory(request);
        SuccessResponse<String> res = new SuccessResponse("Category added successfully");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(res);
    }

    @GetMapping
    public ResponseEntity getAllCategories() {
        SuccessResponse<String> res = new SuccessResponse("Categories fetched successfully",categoryService.getAllCategories());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCategoryById(@PathVariable int id) {
        Category category = categoryService.getCategoryById(id);
        SuccessResponse<Category> res = new SuccessResponse<>("Category retrieved successfully", category);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCategory(
            @PathVariable int id,
            @RequestBody UpdateCategoryRequest request
    ) {
        Category category = categoryService.updateCategory(id, request);
        SuccessResponse<Category> res = new SuccessResponse<>("Category updated successfully", category);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(
            @PathVariable int id
    ) {
        categoryService.deleteCategory(id);
        SuccessResponse<String> res = new SuccessResponse<>("Category deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
