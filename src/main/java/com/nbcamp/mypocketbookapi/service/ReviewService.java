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

	// 리뷰 생성
	@Transactional
	public ReviewResponseDto createReview(Long memberId, Long contentId, ReviewRequestDto requestDto) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

		Content content = contentRepository.findById(contentId)
			.orElseThrow(() -> new ContentException(ErrorCode.CONTENT_NOT_FOUND));

		Review review = Review.builder()
			.member(member)
			.content(content)
			.rating(requestDto.getRating())
			.text(requestDto.getText())
			.build();

		Review savedReview = reviewRepository.save(review);
		return new ReviewResponseDto(savedReview);
	}

	// ISBN 기준 리뷰 목록 조회 (페이징 및 N+1 해결)
	@Transactional(readOnly = true)
	public Page<ReviewResponseDto> getReviewsByIsbn(String isbn, Pageable pageable) {
		Page<Review> reviewsPage = reviewRepository.findByContent_IsbnWithMemberAndContent(isbn, pageable);

		if (reviewsPage.isEmpty() && pageable.getPageNumber() == 0) {
			throw new ReviewException(ErrorCode.REVIEW_NOT_FOUND);
		}

		return reviewsPage.map(ReviewResponseDto::new);
	}

	// 전체 리뷰 조회 (페이징 및 N+1 해결)
	@Transactional(readOnly = true)
	public Page<ReviewResponseDto> getAllReviews(Pageable pageable) {
		Page<Review> reviewsPage = reviewRepository.findAllWithMemberAndContent(pageable);
		return reviewsPage.map(ReviewResponseDto::new);
	}

	// 특정 콘텐츠의 특정 리뷰 단건 조회
	@Transactional(readOnly = true)
	public ReviewResponseDto getReviewByContentIdAndReviewId(Long contentId, Long reviewId) {
		Review review = reviewRepository.findByContentIdAndIdWithMemberAndContent(contentId, reviewId);

		if (review == null) {
			throw new ReviewException(ErrorCode.REVIEW_NOT_FOUND);
		}

		return new ReviewResponseDto(review);
	}

	// 리뷰 수정
	@Transactional
	public ReviewResponseDto updateReview(Long memberId, Long contentId, Long reviewId, ReviewRequestDto requestDto) {
		memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

		contentRepository.findById(contentId)
			.orElseThrow(() -> new ContentException(ErrorCode.CONTENT_NOT_FOUND));

		Review review = reviewRepository.findByContentIdAndIdWithMemberAndContent(contentId, reviewId);
		if (review == null) {
			throw new ReviewException(ErrorCode.REVIEW_NOT_FOUND);
		}

		if (!review.getMember().getId().equals(memberId)) {
			throw new ReviewException(ErrorCode.UNAUTHORIZED_REVIEW_MODIFICATION);
		}

		review.updateReview(requestDto.getRating(), requestDto.getText());
		return new ReviewResponseDto(review);
	}

	// 리뷰 삭제
	@Transactional
	public void deleteReview(Long memberId, Long reviewId) {
		memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

		Review review = reviewRepository.findByIdWithMember(reviewId)
			.orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));

		if (!review.getMember().getId().equals(memberId)) {
			throw new ReviewException(ErrorCode.UNAUTHORIZED_REVIEW_DELETION);
		}

		reviewRepository.delete(review);
	}
}
