package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.common.BaseResponse;
import com.nbcamp.mypocketbookapi.common.LoginMember;
import com.nbcamp.mypocketbookapi.common.ResponseCode;
import com.nbcamp.mypocketbookapi.dto.review.ReviewRequestDto;
import com.nbcamp.mypocketbookapi.dto.review.ReviewResponseDto;
import com.nbcamp.mypocketbookapi.dto.review.TopReviewResponseDto;
import com.nbcamp.mypocketbookapi.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "리뷰 API", description = "리뷰 관련 API")
public class ReviewController {

	private final ReviewService reviewService;

	// 좋아요 많은 리뷰 Top 10 조회
	@Operation(summary = "좋아요 많은 리뷰 Top 10 조회", description = "좋아요가 가장 많은 리뷰 10개를 조회합니다. (결과는 10분간 캐싱됩니다)")
	@GetMapping("/reviews/top10")
	public ResponseEntity<BaseResponse<List<TopReviewResponseDto>>> getTop10LikedReviews() {
		List<TopReviewResponseDto> responseDto = reviewService.getTop10LikedReviews();
		return ResponseEntity
			.status(ResponseCode.SUCCESS_OK.getHttpStatus())
			.body(BaseResponse.success(ResponseCode.SUCCESS_OK, responseDto));
	}

	// 리뷰 작성
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


	// ISBN 기준 리뷰 조회
	@Operation(summary = "ISBN 기준 리뷰 조회", description = "도서 ISBN을 기준으로 리뷰 목록을 조회합니다. 페이징 포함.")
	@GetMapping("/books/{isbn}/reviews")
	public ResponseEntity<BaseResponse<Page<ReviewResponseDto>>> getReviewByIsbn(
		@PathVariable String isbn,
		@ParameterObject Pageable pageable
	) {
		Page<ReviewResponseDto> reviewsPage = reviewService.getReviewsByIsbn(isbn, pageable);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_OK.getHttpStatus())
			.body(BaseResponse.success(ResponseCode.SUCCESS_OK, reviewsPage));
	}


	// 전체 리뷰 조회
	@Operation(summary = "전체 리뷰 조회", description = "등록된 전체 리뷰를 조회합니다. 최신순 정렬, 페이징 지원.")
	@GetMapping("/reviews")
	public ResponseEntity<BaseResponse<Page<ReviewResponseDto>>> getAllReviews(
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
		@ParameterObject Pageable pageable
	) {
		Page<ReviewResponseDto> reviewsPage = reviewService.getAllReviews(pageable);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_OK.getHttpStatus())
			.body(BaseResponse.success(ResponseCode.SUCCESS_OK, reviewsPage));
	}

	// 특정 리뷰 단건 조회
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

	// 리뷰 수정
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

	// 리뷰 삭제
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