package com.nbcamp.mypocketbookapi.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nbcamp.mypocketbookapi.common.LoginMember;
import com.nbcamp.mypocketbookapi.dto.review.ReviewRequestDto;
import com.nbcamp.mypocketbookapi.dto.review.ReviewResponseDto;
import com.nbcamp.mypocketbookapi.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	// 리뷰 작성
	@PostMapping("/contents/{contentId}/reviews")
	public ResponseEntity<ReviewResponseDto> createReview(
		@LoginMember Long memberId, // 세션에서 로그인한 사용자 ID 자동 주입
		// @param memberId 로그인한 사용자ID (@LoginMember 어노테이션으로 세션에서 자동주입)
		@PathVariable Long contentId,
		@RequestBody ReviewRequestDto reviewRequestDto
	) {
		return ResponseEntity.ok(reviewService.createReview(memberId, contentId, reviewRequestDto));
	}

	// ISBN 기준으로 모든 리뷰 조회 (모든 사용자가 해당 책에 등록한 리뷰)
	@GetMapping("/books/{isbn}/reviews")
	public ResponseEntity<List<ReviewResponseDto>> getReviewByIsbn(@PathVariable String isbn) {
		return ResponseEntity.ok(reviewService.getReviewsByIsbn(isbn));
	}

	// 전체 리뷰 조회 (페이징 기능 추가)
	@GetMapping("/reviews")
	public ResponseEntity<Page<ReviewResponseDto>> getAllReviews(
		@PageableDefault(size = 10, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable
	) {
		return ResponseEntity.ok(reviewService.getAllReviews(pageable));
	}

	// 특정 콘텐츠의 특정 리뷰 단건 조회
	@GetMapping("/contents/{contentId}/reviews/{reviewId}")
	public ResponseEntity<ReviewResponseDto> getReviewByContentIdAndReviewId(
		@PathVariable Long contentId,
		@PathVariable Long reviewId
	) {
		return ResponseEntity.ok(reviewService.getReviewByContentIdAndReviewId(contentId, reviewId));
	}

	// 리뷰 수정
	@PutMapping("/contents/{contentId}/reviews/{reviewId}")
	public ResponseEntity<ReviewResponseDto> updateReview(
		@LoginMember Long memberId, // 세션에서 로그인한 사용자 ID 자동 주입
		// @param memberId 로그인한 사용자ID (@LoginMember 어노테이션으로 세션에서 자동 주입)
		@PathVariable Long contentId,
		@PathVariable Long reviewId,
		@RequestBody ReviewRequestDto reviewRequestDto
	) {
		return ResponseEntity.ok(reviewService.updateReview(memberId, contentId, reviewId, reviewRequestDto));
	}

	// 리뷰 삭제
	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<Void> deleteReview(
		@LoginMember Long memberId, // 세션에서 로그인한 사용자 ID 자동주입
		@PathVariable Long reviewId) {
		reviewService.deleteReview(memberId, reviewId);
		return ResponseEntity.noContent().build();
	}

}
