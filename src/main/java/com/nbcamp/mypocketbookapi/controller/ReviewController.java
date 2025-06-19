package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.common.BaseResponse;
import com.nbcamp.mypocketbookapi.common.LoginMember;
import com.nbcamp.mypocketbookapi.common.ResponseCode;
import com.nbcamp.mypocketbookapi.dto.review.ReviewRequestDto;
import com.nbcamp.mypocketbookapi.dto.review.ReviewResponseDto;
import com.nbcamp.mypocketbookapi.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "C. 리뷰", description = "리뷰 관련 API")
public class ReviewController {

	private final ReviewService reviewService;

	@Operation(summary = "리뷰 작성", description = "로그인한 사용자가 특정 콘텐츠에 리뷰를 작성합니다.")
	@PostMapping("/contents/{contentId}/reviews")
	public ResponseEntity<BaseResponse<ReviewResponseDto>> createReview(
		@LoginMember Long memberId,
		@PathVariable Long contentId,
		@RequestBody @Valid ReviewRequestDto reviewRequestDto
	) {
		ReviewResponseDto responseDto = reviewService.createReview(memberId, contentId, reviewRequestDto);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_REVIEW_REGISTERED.getHttpStatus())
			.body(BaseResponse.success(ResponseCode.SUCCESS_REVIEW_REGISTERED, responseDto));
	}

	@Operation(summary = "ISBN 기준 리뷰 조회", description = "도서 ISBN을 기준으로 리뷰 목록을 조회합니다. 페이징 포함.")
	@GetMapping("/books/{isbn}/reviews")
	public ResponseEntity<BaseResponse<Page<ReviewResponseDto>>> getReviewByIsbn(
		@PathVariable String isbn,
		Pageable pageable
	) {
		Page<ReviewResponseDto> reviewsPage = reviewService.getReviewsByIsbn(isbn, pageable);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_OK.getHttpStatus())
			.body(BaseResponse.success(ResponseCode.SUCCESS_OK, reviewsPage));
	}

	@Operation(summary = "전체 리뷰 조회", description = "등록된 전체 리뷰를 조회합니다. 최신순 정렬, 페이징 지원.")
	@GetMapping("/reviews")
	public ResponseEntity<BaseResponse<Page<ReviewResponseDto>>> getAllReviews(
		@PageableDefault(size = 10, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC)
		Pageable pageable
	) {
		Page<ReviewResponseDto> reviewsPage = reviewService.getAllReviews(pageable);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_OK.getHttpStatus())
			.body(BaseResponse.success(ResponseCode.SUCCESS_OK, reviewsPage));
	}

	@Operation(summary = "특정 리뷰 단건 조회", description = "특정 콘텐츠 내 특정 리뷰를 단건 조회합니다.")
	@GetMapping("/contents/{contentId}/reviews/{reviewId}")
	public ResponseEntity<BaseResponse<ReviewResponseDto>> getReviewByContentIdAndReviewId(
		@PathVariable Long contentId,
		@PathVariable Long reviewId
	) {
		ReviewResponseDto responseDto = reviewService.getReviewByContentIdAndReviewId(contentId, reviewId);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_OK.getHttpStatus())
			.body(BaseResponse.success(ResponseCode.SUCCESS_OK, responseDto));
	}

	@Operation(summary = "리뷰 수정", description = "작성자가 자신의 리뷰를 수정합니다.")
	@PutMapping("/contents/{contentId}/reviews/{reviewId}")
	public ResponseEntity<BaseResponse<ReviewResponseDto>> updateReview(
		@LoginMember Long memberId,
		@PathVariable Long contentId,
		@PathVariable Long reviewId,
		@RequestBody @Valid ReviewRequestDto reviewRequestDto
	) {
		ReviewResponseDto responseDto = reviewService.updateReview(memberId, contentId, reviewId, reviewRequestDto);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_OK.getHttpStatus())
			.body(BaseResponse.success(ResponseCode.SUCCESS_OK, responseDto));
	}

	@Operation(summary = "리뷰 삭제", description = "작성자가 자신의 리뷰를 삭제합니다.")
	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<BaseResponse<Void>> deleteReview(
		@LoginMember Long memberId,
		@PathVariable Long reviewId
	) {
		reviewService.deleteReview(memberId, reviewId);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_NO_CONTENT.getHttpStatus())
			.body(BaseResponse.success(ResponseCode.SUCCESS_NO_CONTENT));
	}
}
