package com.electromart.backend.controller;

import com.electromart.backend.dto.ReviewDTO;
import com.electromart.backend.model.Review;
import com.electromart.backend.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @PostMapping("/add")
    public Review addReview(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.addReview(
                reviewDTO.getUserId(),
                reviewDTO.getProductId(),
                reviewDTO.getRating(),
                reviewDTO.getComment()
        );
    }


    @GetMapping("/product/{productId}")
    public List<Review> getReviewsByProduct(@PathVariable Long productId) {
        return reviewService.getReviewsByProduct(productId);
    }


    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}