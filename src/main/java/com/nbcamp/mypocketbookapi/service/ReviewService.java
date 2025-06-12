package com.nbcamp.mypocketbookapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nbcamp.mypocketbookapi.dto.review.ReviewRequestDto;
import com.nbcamp.mypocketbookapi.dto.review.ReviewResponseDto;
import com.nbcamp.mypocketbookapi.entity.Content;
import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.entity.Review;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;
import com.nbcamp.mypocketbookapi.exception.content.ContentException;
import com.nbcamp.mypocketbookapi.exception.member.MemberException;
import com.nbcamp.mypocketbookapi.exception.review.ReviewException;
import com.nbcamp.mypocketbookapi.repository.ContentJpaRepository;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import com.nbcamp.mypocketbookapi.repository.ReviewJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewJpaRepository reviewRepository;
	private final MemberJpaRepository memberRepository;
	private final ContentJpaRepository contentRepository;

	// 리뷰 작성
	@Transactional
	public ReviewResponseDto createReview(Long memberId, Long contentId, ReviewRequestDto requestDto) {
		// 회원 존재 여부 확인
		Member member = memberRepository.findById(memberId).orElseThrow(
			() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND)
		);

		// 콘텐츠 존재 여부 확인
		Content content = contentRepository.findById(contentId).orElseThrow(
			() -> new ContentException(ErrorCode.CONTENT_NOT_FOUND)
		);

		// 리뷰 엔티티 생성 및 저장
		Review review = Review.builder()
			.member(member)
			.content(content)
			.rating(requestDto.getRating())
			.text(requestDto.getText())
			.build();

		Review savedReview = reviewRepository.save(review);

		return new ReviewResponseDto(savedReview);
	}

	// ISBN 기준으로 모든 리뷰 조회 (페이징 추가 및 N+1 문제 해결)
	@Transactional(readOnly = true)
	public Page<ReviewResponseDto> getReviewsByIsbn(String isbn, Pageable pageable) {
		// N+1 문제가 해결된 쿼리 메서드 사용
		Page<Review> reviewsPage = reviewRepository.findByContent_IsbnWithMemberAndContent(isbn, pageable);

		// 첫 페이지를 요청했고, 리뷰가 없을 경우 예외 처리
		if (reviewsPage.isEmpty() && pageable.getPageNumber() == 0) {
			throw new ReviewException(ErrorCode.REVIEW_NOT_FOUND);
		}

		// Review 엔티티를 ReviewResponseDto로 변환하여 반환
		return reviewsPage.map(ReviewResponseDto::new);
	}

	// 전체 리뷰 조회 (페이징 기능 추가 및 N+1 문제 해결)
	@Transactional(readOnly = true)
	public Page<ReviewResponseDto> getAllReviews(Pageable pageable) {
		// N+1 문제가 해결된 쿼리 메서드 사용
		Page<Review> reviewsPage = reviewRepository.findAllWithMemberAndContent(pageable);

		// Review 엔티티를 ReviewResponseDto로 변환하여 반환
		return reviewsPage.map(ReviewResponseDto::new);
	}

	// 특정 콘텐츠의 특정 리뷰 단건 조회 (N+1 문제 해결)
	@Transactional(readOnly = true)
	public ReviewResponseDto getReviewByContentIdAndReviewId(Long contentId, Long reviewId) {
		// 콘텐츠 존재 여부 확인 (리뷰가 해당 콘텐츠에 속하는지 확인하기 위함)
		contentRepository.findById(contentId).orElseThrow(
			() -> new ContentException(ErrorCode.CONTENT_NOT_FOUND)
		);

		// N+1 문제가 해결된 쿼리 메서드를 사용하여 리뷰 조회
		Review review = reviewRepository.findByContentIdAndIdWithMemberAndContent(contentId, reviewId);

		if (review == null) {
			throw new ReviewException(ErrorCode.REVIEW_NOT_FOUND);
		}

		return new ReviewResponseDto(review);
	}

	// 리뷰 수정 (N+1 문제 해결)
	@Transactional
	public ReviewResponseDto updateReview(Long memberId, Long contentId, Long reviewId, ReviewRequestDto requestDto) {
		// 회원 존재 여부 확인
		memberRepository.findById(memberId).orElseThrow(
			() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND)
		);

		// 콘텐츠 존재 여부 확인
		contentRepository.findById(contentId).orElseThrow(
			() -> new ContentException(ErrorCode.CONTENT_NOT_FOUND)
		);

		// N+1 문제가 해결된 쿼리 (리뷰와 회원 정보를 함께 Fetch)를 사용하여 리뷰 조회
		// 이를 통해 review.getMember().getId() 호출 시 추가 쿼리 발생 방지
		Review review = reviewRepository.findByContentIdAndIdWithMember(contentId, reviewId);
		if (review == null) {
			throw new ReviewException(ErrorCode.REVIEW_NOT_FOUND);
		}

		// 리뷰 작성자만 수정 가능
		if (!review.getMember().getId().equals(memberId)) {
			throw new ReviewException(ErrorCode.UNAUTHORIZED_REVIEW_MODIFICATION);
		}

		// 리뷰 내용 업데이트
		review.updateReview(requestDto.getRating(), requestDto.getText());

		return new ReviewResponseDto(review);
	}

	// 리뷰 삭제 (N+1 문제 해결)
	@Transactional
	public void deleteReview(Long memberId, Long reviewId) {
		// 회원 존재 여부 확인
		memberRepository.findById(memberId).orElseThrow(
			() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND)
		);

		// N+1 문제가 해결된 쿼리 (리뷰와 회원 정보를 함께 Fetch)를 사용하여 리뷰 조회
		// 이를 통해 review.getMember().getId() 호출 시 추가 쿼리 발생 방지
		Review review = reviewRepository.findByIdWithMember(reviewId);
		if (review == null) {
			throw new ReviewException(ErrorCode.REVIEW_NOT_FOUND);
		}

		// 리뷰 작성자만 삭제 가능
		if (!review.getMember().getId().equals(memberId)) {
			throw new ReviewException(ErrorCode.UNAUTHORIZED_REVIEW_DELETION);
		}

		// 리뷰 삭제
		reviewRepository.delete(review);
	}
}