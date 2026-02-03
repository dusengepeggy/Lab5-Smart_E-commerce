package com.example.e_commerce.dto.ResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductWithCategory (
    int product_id,
    int category_id,
    String category_name,
    String name,
    String description,
    BigDecimal price,
    LocalDateTime created_at
){}
