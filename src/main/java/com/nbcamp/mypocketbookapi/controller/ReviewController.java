package com.nbcamp.mypocketbookapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nbcamp.mypocketbookapi.dto.ReviewRequestDto;
import com.nbcamp.mypocketbookapi.dto.ReviewResponseDto;
import com.nbcamp.mypocketbookapi.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	// 리뷰작성
	@PostMapping("/api/contents/{contentId}/reviews")
	public ResponseEntity<ReviewResponseDto> createReview(
		@PathVariable Long contentId,
		@RequestBody ReviewRequestDto reviewRequestDto // 리퀘스트바디 임포트??방법??
	) {
		Long memberId = 1L;
		return ResponseEntity.ok(reviewService.createReview(memberId, contentId, reviewRequestDto));
	}
}