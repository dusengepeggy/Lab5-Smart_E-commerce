package com.example.e_commerce.controller;

import com.example.e_commerce.dto.JsonResponseDto.SuccessResponse;
import com.example.e_commerce.dto.RequestDto.UpdateCategoryRequest;
import com.example.e_commerce.dto.ResponseDto.UserDTO;
import com.example.e_commerce.model.Category;
import com.example.e_commerce.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
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
    public ResponseEntity getCategoryById(
            @PathVariable int id
    ) {
        SuccessResponse<String> res = new SuccessResponse("User account deleted successfully");
        return  ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCategory(
            @PathVariable int id,
            @RequestBody UpdateCategoryRequest request
    ) {
        Category category = categoryService.updateCategory(id, request);
        SuccessResponse<UserDTO> res = new SuccessResponse("User account updated successfully",category);
        return  ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(
            @PathVariable int id
    ) {
        categoryService.deleteCategory(id);
        SuccessResponse<String> res = new SuccessResponse("User account deleted successfully");
        return  ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
