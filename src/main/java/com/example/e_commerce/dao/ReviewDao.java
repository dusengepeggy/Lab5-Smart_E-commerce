package com.example.e_commerce.dao;

import com.example.e_commerce.dto.RequestDto.CreateReviewRequest;
import com.example.e_commerce.dto.RequestDto.UpdateReviewRequest;
import com.example.e_commerce.model.Review;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ReviewDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Review> REVIEW_ROW_MAPPER = (rs, rowNum) ->
            new Review(
                    rs.getInt("review_id"),
                    rs.getInt("user_id"),
                    rs.getInt("product_id"),
                    rs.getInt("rating"),
                    rs.getString("comment"),
                    rs.getDate("review_date")
            );


    public void createReview(CreateReviewRequest review) {
        jdbcTemplate.update(
                """
                INSERT INTO Review (user_id, product_id, rating, comment)
                VALUES (?, ?, ?, ?)
                """,
                review.getUserId(),
                review.getProductId(),
                review.getRating(),
                review.getComment()
        );
    }


    public List<Review> getAllReviews() {
        return jdbcTemplate.query(
                "SELECT * FROM Review ORDER BY review_date DESC, review_id DESC",
                REVIEW_ROW_MAPPER
        );
    }

    public Optional<Review> getReviewById(int reviewId) {
        try {
            Review review = jdbcTemplate.queryForObject(
                    "SELECT * FROM Review WHERE review_id = ?",
                    REVIEW_ROW_MAPPER,
                    reviewId
            );
            return Optional.ofNullable(review);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Review> getReviewsByProductId(int productId) {
        return jdbcTemplate.query(
                "SELECT * FROM Review WHERE product_id = ? ORDER BY review_date DESC",
                REVIEW_ROW_MAPPER,
                productId
        );
    }

    public List<Review> getReviewsByUserId(int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM Review WHERE user_id = ? ORDER BY review_date DESC",
                REVIEW_ROW_MAPPER,
                userId
        );
    }


    public int updateReview(int reviewId, UpdateReviewRequest review) {
        StringBuilder sql = new StringBuilder("UPDATE Review SET ");
        List<Object> params = new ArrayList<>();

        if (review.getRating() != null) {
            sql.append("rating = ?, ");
            params.add(review.getRating());
        }

        if (review.getComment() != null) {
            sql.append("comment = ?, ");
            params.add(review.getComment());
        }

        sql.setLength(sql.length() - 2); // remove trailing comma
        sql.append(" WHERE review_id = ?");
        params.add(reviewId);

        return jdbcTemplate.update(sql.toString(), params.toArray());
    }

    public int deleteReview(int reviewId) {
        return jdbcTemplate.update(
                "DELETE FROM Review WHERE review_id = ?",
                reviewId
        );
    }
}
