package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.common.BaseResponse;
import com.nbcamp.mypocketbookapi.common.LoginMember;
import com.nbcamp.mypocketbookapi.common.ResponseCode;
import com.nbcamp.mypocketbookapi.dto.review.ReviewRequestDto;
import com.nbcamp.mypocketbookapi.dto.review.ReviewResponseDto;
import com.nbcamp.mypocketbookapi.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

@Tag(name = "리뷰 관리", description = "리뷰 관련 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	// 리뷰 작성
	@Operation(summary = "리뷰 작성", description = "특정 도서(콘텐츠)에 대한 리뷰를 작성합니다")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "리뷰 작성 성공"),
		@ApiResponse(responseCode = "404", description = "요청한 회원 또는 콘텐츠를 찾을 수 없음")
	})
	@PostMapping("/contents/{contentId}/reviews")
	public ResponseEntity<BaseResponse<ReviewResponseDto>> createReview(
		@Parameter(hidden = true) @LoginMember Long memberId,
		@Parameter(description = "리뷰를 작성할 콘텐츠(도서)의 ID", required = true)
		@PathVariable Long contentId,
		@RequestBody ReviewRequestDto reviewRequestDto
	) {
		ReviewResponseDto responseDto = reviewService.createReview(memberId, contentId, reviewRequestDto);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_REVIEW_REGISTERED.getHttpStatus())
			.body(BaseResponse.success(ResponseCode.SUCCESS_REVIEW_REGISTERED, responseDto));
	}

	// ISBN 기준으로 모든 리뷰 조회 (페이징 기능 포함)
	@Operation(summary = "ISBN으로 리뷰 조회", description = "특정 ISBN을 가진 도서의 모든 리뷰를 페이징하여 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "리뷰 조회 성공"),
		@ApiResponse(responseCode = "404", description = "해당 ISBN의 도서에 작성된 리뷰가 없음")
	})
	@GetMapping("/books/{isbn}/reviews")
	public ResponseEntity<BaseResponse<Page<ReviewResponseDto>>> getReviewByIsbn(
		@Parameter(description = "조회할 도서의 ISBN", required = true)
		@PathVariable String isbn,
		Pageable pageable
	) {
		Page<ReviewResponseDto> reviewsPage = reviewService.getReviewsByIsbn(isbn, pageable);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_OK.getHttpStatus())
			.body(BaseResponse.success(ResponseCode.SUCCESS_OK, reviewsPage));
	}

	// 전체 리뷰 조회 (페이징 기능 포함)
	@Operation(summary = "전체 리뷰 조회", description = "시스템에 등록된 모든 리뷰를 최신순으로 페이징하여 조회합니다.")
	@ApiResponse(responseCode = "200", description = "전체 리뷰 조회 성공")
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

	// 특정 콘텐츠의 특정 리뷰 단건 조회
	@Operation(summary = "특정 리뷰 단건 조회", description = "특정 콘텐츠에 속한 특정 리뷰 하나를 상세 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "리뷰 상세 조회 성공"),
		@ApiResponse(responseCode = "404", description = "요청한 콘텐츠 또는 리뷰를 찾을 수 없음")
	})
	@GetMapping("/contents/{contentId}/reviews/{reviewId}")
	public ResponseEntity<BaseResponse<ReviewResponseDto>> getReviewByContentIdAndReviewId(
		@Parameter(description = "리뷰가 속한 콘텐츠(도서)의 ID", required = true)
		@PathVariable Long contentId,
		@Parameter(description = "조회할 리뷰의 ID", required = true)
		@PathVariable Long reviewId
	) {
		ReviewResponseDto responseDto = reviewService.getReviewByContentIdAndReviewId(contentId, reviewId);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_OK.getHttpStatus())
			.body(BaseResponse.success(ResponseCode.SUCCESS_OK, responseDto));
	}

	// 리뷰 수정
	@Operation(summary = "리뷰 수정", description = "작성했던 리뷰의 별점이나 내용을 수정합니다. 본인만 수정할 수 있습니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "리뷰 수정 성공"),
		@ApiResponse(responseCode = "403", description = "리뷰를 수정할 권한이 없음"),
		@ApiResponse(responseCode = "404", description = "요청한 리뷰를 찾을 수 없음")
	})
	@PutMapping("/contents/{contentId}/reviews/{reviewId}")
	public ResponseEntity<BaseResponse<ReviewResponseDto>> updateReview(
		@Parameter(hidden = true) @LoginMember Long memberId,
		@Parameter(description = "리뷰가 속한 콘텐츠(도서)의 ID", required = true)
		@PathVariable Long contentId,
		@Parameter(description = "수정할 리뷰의 ID", required = true)
		@PathVariable Long reviewId,
		@RequestBody ReviewRequestDto reviewRequestDto
	) {
		ReviewResponseDto responseDto = reviewService.updateReview(memberId, contentId, reviewId, reviewRequestDto);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_OK.getHttpStatus())
			.body(BaseResponse.success(ResponseCode.SUCCESS_OK, responseDto));
	}

	// 리뷰 삭제
	@Operation(summary = "리뷰 삭제", description = "작성했던 리뷰를 삭제합니다. 본인만 삭제할 수 있습니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "리뷰 삭제 성공"),
		@ApiResponse(responseCode = "403", description = "리뷰를 삭제할 권한이 없음"),
		@ApiResponse(responseCode = "404", description = "요청한 리뷰를 찾을 수 없음")
	})
	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<BaseResponse<Void>> deleteReview(
		@Parameter(hidden = true) @LoginMember Long memberId,
		@Parameter(description = "삭제할 리뷰의 ID", required = true)
		@PathVariable Long reviewId
	) {
		reviewService.deleteReview(memberId, reviewId);
		return ResponseEntity
			.status(ResponseCode.SUCCESS_NO_CONTENT.getHttpStatus())
			.body(BaseResponse.success(ResponseCode.SUCCESS_NO_CONTENT));
	}
}