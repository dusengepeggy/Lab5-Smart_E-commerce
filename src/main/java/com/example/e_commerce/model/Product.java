package com.example.e_commerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private int product_id;
    private int category_id;
    private String name;
    private String description;
    private BigDecimal price;
    private Date created_at;

}
