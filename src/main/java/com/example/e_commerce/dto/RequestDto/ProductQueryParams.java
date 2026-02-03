package com.example.e_commerce.dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductQueryParams {
    private Integer categoryId;
    private String q;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean inStock;
    private int page = 0;
    private int size = 20;
    private String sortBy = "name";
    private String sortDir = "asc";
}
