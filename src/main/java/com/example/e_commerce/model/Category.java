package com.example.e_commerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Long category_id;
    private String category_name;
    private String description;
}
