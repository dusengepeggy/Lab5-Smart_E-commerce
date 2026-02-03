package com.example.e_commerce.service;

import com.example.e_commerce.dao.ReviewDao;
import com.example.e_commerce.dto.RequestDto.CreateReviewRequest;
import com.example.e_commerce.dto.RequestDto.UpdateReviewRequest;
import com.example.e_commerce.model.Review;
import com.example.e_commerce.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewDao reviewDao;

    public void createReview(int userId, int productId, int rating, String comment) {
        CreateReviewRequest review = new CreateReviewRequest();
        review.setUserId(userId);
        review.setProductId(productId);
        review.setRating(rating);
        review.setComment(comment);
        reviewDao.createReview(review);
    }

    public Review getReviewById(int reviewId) {
        return reviewDao.getReviewById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review with ID " + reviewId + " not found"));
    }

    public List<Review> getAllReviews() {
        return reviewDao.getAllReviews();
    }

    public List<Review> getReviewsByProductId(int productId) {
        return reviewDao.getReviewsByProductId(productId);
    }

    public List<Review> getReviewsByUserId(int userId) {
        return reviewDao.getReviewsByUserId(userId);
    }

    public Review updateReview(int reviewId, int rating, String comment) {
        Review existing = getReviewById(reviewId);
        UpdateReviewRequest update = new UpdateReviewRequest();
        update.setRating(rating > 0 ? rating : existing.getRating());
        update.setComment(comment != null ? comment : existing.getComment());
        int rows = reviewDao.updateReview(reviewId, update);
        if (rows == 0) {
            throw new NotFoundException("Review with ID " + reviewId + " not found");
        }
        return getReviewById(reviewId);
    }

    public void deleteReview(int reviewId) {
        int rows = reviewDao.deleteReview(reviewId);
        if (rows == 0) {
            throw new NotFoundException("Review with ID " + reviewId + " not found");
        }
    }
}
