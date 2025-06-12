package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.common.BaseResponse;
import com.nbcamp.mypocketbookapi.common.LoginMember;
import com.nbcamp.mypocketbookapi.common.ResponseCode;
import com.nbcamp.mypocketbookapi.dto.reviewlike.ReviewLikeResponseDto;
import com.nbcamp.mypocketbookapi.service.ReviewLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @PostMapping("/api/reviews/{reviewId}/likes")
    public ResponseEntity<BaseResponse<Void>> likeReview(@PathVariable Long reviewId, @LoginMember Long memberId) {
        reviewLikeService.reviewLike(memberId, reviewId);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success(ResponseCode.SUCCESS_REVIEW_LIKE_REGISTERED));
    }

    @DeleteMapping("/api/reviews/{reviewId}/likes")
    public ResponseEntity<BaseResponse<Void>> deleteReview(@PathVariable Long reviewId, @LoginMember Long memberId) {
        reviewLikeService.deleteReviewLike(memberId, reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(ResponseCode.SUCCESS_REVIEW_LIKE_REMOVED));
    }

    @GetMapping("/api/likes/reviews")
    public ResponseEntity<BaseResponse<List<ReviewLikeResponseDto>>> checkAllReviewLikes(@LoginMember Long memberId) {
        List<ReviewLikeResponseDto> likeList = reviewLikeService.checkAllReviewLikes(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(ResponseCode.SUCCESS_OK, likeList));
    }
}