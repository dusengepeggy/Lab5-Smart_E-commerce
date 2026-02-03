package com.example.e_commerce.dto.RequestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProductRequest {
    @NotBlank(message = "Category is required")
    private int category_id;
    @NotBlank(message = "Product name is required")
    private String name;
    @Size(min = 100, message = "Description should be more than 5 characters")
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Price is required")
    private BigDecimal price;
}
