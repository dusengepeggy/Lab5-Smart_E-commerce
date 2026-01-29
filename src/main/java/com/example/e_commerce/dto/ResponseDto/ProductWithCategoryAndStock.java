package com.example.e_commerce.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithCategoryAndStock {
    private int product_id;
    private int category_id;
    private String category_name;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDateTime created_at;
    private int stock_quantity;
}
