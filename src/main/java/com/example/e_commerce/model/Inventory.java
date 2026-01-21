package com.example.e_commerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    private int inventory_id;
    private int product_id;
    private int stock_quantity;
    private String warehouse_location;
    private Timestamp updated_at;
}
