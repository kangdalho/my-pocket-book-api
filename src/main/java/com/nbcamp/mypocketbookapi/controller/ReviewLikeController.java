package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.service.ReviewLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @PostMapping("/api/reviews/{reviewId}/likes")
    public ResponseEntity<String> likeReview(@PathVariable Long reviewId, @RequestParam Long memberId) {
        reviewLikeService.reviewLike(memberId, reviewId);
        return ResponseEntity.ok("리뷰에 좋아요를 눌렀습니다.");
    }

    @DeleteMapping("/api/reviews/{reviewId}/likes")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId, @RequestParam Long memberId) {
        reviewLikeService.deleteReviewLike(memberId, reviewId);
        return ResponseEntity.ok("리뷰에 좋아요가 취소되었습니다.");
    }
}