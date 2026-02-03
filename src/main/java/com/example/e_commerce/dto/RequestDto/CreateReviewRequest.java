package com.example.e_commerce.dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewRequest {

    private int userId;
    private int productId;
    private int rating;
    private String comment;
}
