package com.nbcamp.mypocketbookapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nbcamp.mypocketbookapi.dto.review.ReviewRequestDto;
import com.nbcamp.mypocketbookapi.dto.review.ReviewResponseDto;
import com.nbcamp.mypocketbookapi.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	// 리뷰 작성
	@PostMapping("/api/contents/{contentId}/reviews")
	public ResponseEntity<ReviewResponseDto> createReview(
		@PathVariable Long contentId,
		@RequestBody ReviewRequestDto reviewRequestDto
	) {
		Long memberId = 1L; // 임시 사용자 ID
		return ResponseEntity.ok(reviewService.createReview(memberId, contentId, reviewRequestDto));
	}

	// 특정 콘텐츠의 모든 리뷰 조회
	@GetMapping("/api/contents/{contentId}/reviews")
	public ResponseEntity<List<ReviewResponseDto>> getReviewsByContentId(@PathVariable Long contentId) {
		return ResponseEntity.ok(reviewService.getReviewsByContentId(contentId));
	}



	// 특정 콘텐츠의 특정 리뷰 단건 조회
	@GetMapping("/api/contents/{contentId}/reviews/{reviewId}")
	public ResponseEntity<ReviewResponseDto> getReviewByContentIdAndReviewId(
		@PathVariable Long contentId,
		@PathVariable Long reviewId
	) {
		return ResponseEntity.ok(reviewService.getReviewByContentIdAndReviewId(contentId, reviewId));
	}

	// 리뷰 수정
	@PutMapping("/api/contents/{contentId}/reviews/{reviewId}")
	public ResponseEntity<ReviewResponseDto> updateReview(
		@PathVariable Long contentId,
		@PathVariable Long reviewId,
		@RequestBody ReviewRequestDto reviewRequestDto
	) {
		Long memberId = 1L;  // 임시 하드코딩
		return ResponseEntity.ok(reviewService.updateReview(memberId, contentId, reviewId, reviewRequestDto));
	}
}
