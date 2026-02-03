package com.example.e_commerce.controller;

import com.example.e_commerce.dto.JsonResponseDto.SuccessResponse;
import com.example.e_commerce.dto.RequestDto.AddProductRequest;
import com.example.e_commerce.dto.RequestDto.ProductQueryParams;
import com.example.e_commerce.dto.ResponseDto.PagedResponse;
import com.example.e_commerce.dto.ResponseDto.ProductWithCategory;
import com.example.e_commerce.dto.ResponseDto.ProductWithCategoryAndStock;
import com.example.e_commerce.model.Product;
import com.example.e_commerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
@Tag(name = "Products", description = "Product listing, create, update, delete")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity getProducts(ProductQueryParams params) {
        PagedResponse<ProductWithCategory> data = productService.getAllProductsWithCategory(params);
        SuccessResponse<PagedResponse<ProductWithCategory>> res =
                new SuccessResponse<>("Products fetched successfully", data);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity getProductById(@PathVariable int id) {
        ProductWithCategoryAndStock product = productService.getProductByIdWithCategoryAndStock(id);
        SuccessResponse<ProductWithCategoryAndStock> res =
                new SuccessResponse<>("Product retrieved successfully", product);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping
    public ResponseEntity createProduct(@Valid @RequestBody AddProductRequest request) {
        productService.createProduct(request);
        SuccessResponse<String> res = new SuccessResponse<>("Product created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateProduct(@PathVariable int id, @RequestBody Product product) {
        Product updated = productService.updateProduct(id, product);
        SuccessResponse<Product> res = new SuccessResponse<>("Product updated successfully", updated);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        SuccessResponse<String> res = new SuccessResponse<>("Product deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
