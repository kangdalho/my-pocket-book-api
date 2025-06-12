package com.nbcamp.mypocketbookapi.controller;

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

import com.nbcamp.mypocketbookapi.common.BaseResponse;
import com.nbcamp.mypocketbookapi.common.LoginMember;
import com.nbcamp.mypocketbookapi.common.ResponseCode;
import com.nbcamp.mypocketbookapi.dto.review.ReviewRequestDto;
import com.nbcamp.mypocketbookapi.dto.review.ReviewResponseDto;
import com.nbcamp.mypocketbookapi.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	// 리뷰 작성 엔드포인트
	@PostMapping("/contents/{contentId}/reviews")
	public ResponseEntity<BaseResponse<ReviewResponseDto>> createReview( // 반환 타입 변경
		@LoginMember Long memberId,
		@PathVariable Long contentId,
		@RequestBody ReviewRequestDto reviewRequestDto
	) {
		ReviewResponseDto responseDto = reviewService.createReview(memberId, contentId, reviewRequestDto);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_REVIEW_REGISTERED.getHttpStatus())
			.body(BaseResponse.success(ResponseCode.SUCCESS_REVIEW_REGISTERED, responseDto)); // 공통 응답 적용
	}

	// ISBN 기준으로 모든 리뷰 조회 (페이징 기능 추가)
	@GetMapping("/books/{isbn}/reviews")
	public ResponseEntity<BaseResponse<Page<ReviewResponseDto>>> getReviewByIsbn( // 반환 타입 변경
		@PathVariable String isbn,
		Pageable pageable
	) {
		Page<ReviewResponseDto> reviewsPage = reviewService.getReviewsByIsbn(isbn, pageable);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_OK.getHttpStatus()) // 200 OK
			.body(BaseResponse.success(ResponseCode.SUCCESS_OK, reviewsPage)); // 공통 응답 적용
	}

	// 전체 리뷰 조회 (페이징 기능 추가)
	@GetMapping("/reviews")
	public ResponseEntity<BaseResponse<Page<ReviewResponseDto>>> getAllReviews( // 반환 타입 변경
		@PageableDefault(size = 10, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable
	) {
		Page<ReviewResponseDto> reviewsPage = reviewService.getAllReviews(pageable);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_OK.getHttpStatus()) // 200 OK
			.body(BaseResponse.success(ResponseCode.SUCCESS_OK, reviewsPage)); // 공통 응답 적용
	}

	// 특정 콘텐츠의 특정 리뷰 단건 조회 엔드포인트
	@GetMapping("/contents/{contentId}/reviews/{reviewId}")
	public ResponseEntity<BaseResponse<ReviewResponseDto>> getReviewByContentIdAndReviewId( // 반환 타입 변경
		@PathVariable Long contentId,
		@PathVariable Long reviewId
	) {
		ReviewResponseDto responseDto = reviewService.getReviewByContentIdAndReviewId(contentId, reviewId);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_OK.getHttpStatus()) // 200 OK
			.body(BaseResponse.success(ResponseCode.SUCCESS_OK, responseDto)); // 공통 응답 적용
	}

	// 리뷰 수정 엔드포인트
	@PutMapping("/contents/{contentId}/reviews/{reviewId}")
	public ResponseEntity<BaseResponse<ReviewResponseDto>> updateReview( // 반환 타입 변경
		@LoginMember Long memberId,
		@PathVariable Long contentId,
		@PathVariable Long reviewId,
		@RequestBody ReviewRequestDto reviewRequestDto
	) {
		ReviewResponseDto responseDto = reviewService.updateReview(memberId, contentId, reviewId, reviewRequestDto);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_OK.getHttpStatus()) // 200 OK
			.body(BaseResponse.success(ResponseCode.SUCCESS_OK, responseDto)); // 공통 응답 적용
	}

	// 리뷰 삭제 엔드포인트
	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<BaseResponse<Void>> deleteReview( // 반환 타입 변경
		@LoginMember Long memberId,
		@PathVariable Long reviewId) {
		reviewService.deleteReview(memberId, reviewId);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_NO_CONTENT.getHttpStatus()) // 204 No Content
			.body(BaseResponse.success(ResponseCode.SUCCESS_NO_CONTENT)); // 공통 응답 적용 (데이터 없음)
	}
}