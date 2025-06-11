package com.nbcamp.mypocketbookapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nbcamp.mypocketbookapi.dto.review.ReviewRequestDto;
import com.nbcamp.mypocketbookapi.dto.review.ReviewResponseDto;
import com.nbcamp.mypocketbookapi.entity.Content;
import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.entity.Review;
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
	@Transactional // 트랜잭션 단위로 동작하도록 설정합니다.
	public ReviewResponseDto createReview(Long memberId, Long contentId, ReviewRequestDto requestDto ) {

		// memberRepository.findById(memberId)
		// SELECT m.* FROM member m WHERE m.id = ?
		// 회원 ID로 회원을 조회합니다. 없으면 예외를 발생시킵니다.
		Member member = memberRepository.findById(memberId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 회원입니다")
		);

		// contentRepository.findById(contentId)
		// SELECT c.* FROM content c WHERE c.id = ?
		// 콘텐츠 ID로 콘텐츠를 조회합니다. 없으면 예외를 발생시킵니다.
		Content content = contentRepository.findById(contentId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 콘텐츠입니다")
		);

		// Review 엔티티를 빌더 패턴을 사용하여 생성합니다.
		Review review = Review.builder()
			.member(member)
			.content(content)
			.rating(requestDto.getRating())
			.text(requestDto.getText())
			.build();

		// reviewRepository.save(review)
		// INSERT INTO review (member_id, content_id, rating, text) VALUES (?, ?, ?, ?)
		// 생성된 리뷰를 데이터베이스에 저장합니다.
		Review savedReview = reviewRepository.save(review);

		// 저장된 리뷰 정보를 바탕으로 응답 DTO를 생성하여 반환합니다.
		return new ReviewResponseDto(savedReview); // <-- 이 부분이 메소드의 끝에 있어야 합니다.
	}

	// ISBN 기준으로 모든 리뷰 조회 (페이징 추가 및 N+1 문제 개선)
	@Transactional(readOnly = true) // 읽기 전용 트랜잭션으로 설정하여 성능을 최적화합니다.
	public Page<ReviewResponseDto> getReviewsByIsbn(String isbn, Pageable pageable) {
		// ... (기존 코드 유지)
		Page<Review> reviewsPage = reviewRepository.findByContent_IsbnWithMemberAndContent(isbn, pageable);

		if(reviewsPage.isEmpty() && pageable.getPageNumber() == 0) {
			throw new RuntimeException("해당 ISBN에 대한 리뷰가 존재하지 않습니다");
		}

		return reviewsPage.map(ReviewResponseDto::new);
	}

	// 전체 리뷰 조회 (페이징 기능 추가 및 N+1 문제 개선)
	@Transactional(readOnly = true) // 읽기 전용 트랜잭션으로 설정하여 성능을 최적화합니다.
	public Page<ReviewResponseDto> getAllReviews(Pageable pageable) {
		// ... (기존 코드 유지)
		Page<Review> reviewsPage = reviewRepository.findAllWithMemberAndContent(pageable);
		return reviewsPage.map(ReviewResponseDto::new);
	}


	// 특정 콘텐츠의 특정 리뷰 단건 조회
	@Transactional(readOnly = true) // 읽기 전용 트랜잭션으로 설정합니다.
	public ReviewResponseDto getReviewByContentIdAndReviewId(Long contentId, Long reviewId) {
		// ... (기존 코드 유지)
		contentRepository.findById(contentId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 콘텐츠입니다")
		);

		Review review = reviewRepository.findByContentIdAndId(contentId, reviewId);

		if (review == null) {
			throw new RuntimeException("해당 콘텐츠에서 리뷰를 찾을 수 없습니다");
		}

		return new ReviewResponseDto(review);
	}

	// 리뷰 수정
	@Transactional // 트랜잭션 단위로 동작하도록 설정합니다.
	public ReviewResponseDto updateReview(Long memberId, Long contentId, Long reviewId, ReviewRequestDto requestDto) {
		// ... (기존 코드 유지)
		memberRepository.findById(memberId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 회원입니다")
		);

		contentRepository.findById(contentId).orElseThrow(
			()-> new RuntimeException("존재하지 않는 콘텐츠입니다")
		);

		Review review = reviewRepository.findByContentIdAndId(contentId, reviewId);
		if (review == null) {
			throw new RuntimeException("해당 콘텐츠에서 리뷰를 찾을 수 없습니다");
		}

		if (!review.getMember().getId().equals(memberId)) {
			throw new RuntimeException("리뷰 작성자만 수정할 수 있습니다");
		}

		review.updateReview(requestDto.getRating(), requestDto.getText());

		return new ReviewResponseDto(review);
	}

	// 리뷰 삭제
	@Transactional // 트랜잭션 단위로 동작하도록 설정합니다.
	public void deleteReview(Long memberId, Long reviewId) {
		// ... (기존 코드 유지)
		memberRepository.findById(memberId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 회원입니다")
		);

		Review review = reviewRepository.findById(reviewId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 리뷰 입니다")
		);

		if(!review.getMember().getId().equals(memberId)) {
			throw new RuntimeException("리뷰 작성자만 삭제할 수 있습니다");
		}

		reviewRepository.delete(review);
	}
}