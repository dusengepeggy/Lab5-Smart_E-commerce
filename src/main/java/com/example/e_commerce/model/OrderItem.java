package com.example.e_commerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private int order_item_id;
    private int order_id;
    private int product_id;
    private int quantity;
    private BigDecimal unit_price;
}
