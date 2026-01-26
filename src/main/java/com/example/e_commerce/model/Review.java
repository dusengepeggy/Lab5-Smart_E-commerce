package com.example.e_commerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private int review_id;
    private int user_id;
    private int product_id;
    private int rating;
    private String comment;
    private Date review_date;
}
