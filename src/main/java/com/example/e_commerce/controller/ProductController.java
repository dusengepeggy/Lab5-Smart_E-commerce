package com.example.e_commerce.controller;

import com.example.e_commerce.dto.JsonResponseDto.SuccessResponse;
import com.example.e_commerce.dto.RequestDto.ProductQueryParams;
import com.example.e_commerce.dto.ResponseDto.PagedResponse;
import com.example.e_commerce.dto.ResponseDto.ProductWithCategory;
import com.example.e_commerce.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity getProducts(
            ProductQueryParams params
    ) {
        PagedResponse<ProductWithCategory> data = productService.getAllProductsWithCategory(params);
        SuccessResponse<PagedResponse<ProductWithCategory>> res =
                new SuccessResponse<>("Products fetched successfully", data);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
