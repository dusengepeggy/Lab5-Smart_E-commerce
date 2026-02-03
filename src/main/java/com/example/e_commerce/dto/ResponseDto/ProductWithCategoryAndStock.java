package com.example.e_commerce.dto.ResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductWithCategoryAndStock(
        int productId,
        int categoryId,
        String categoryName,
        String name,
        String description,
        BigDecimal price,
        LocalDateTime createdAt,
        int stockQuantity
) {}

