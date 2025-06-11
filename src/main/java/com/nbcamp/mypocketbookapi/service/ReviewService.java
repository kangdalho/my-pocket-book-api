package com.nbcamp.mypocketbookapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nbcamp.mypocketbookapi.dto.review.ReviewRequestDto;
import com.nbcamp.mypocketbookapi.dto.review.ReviewResponseDto;
import com.nbcamp.mypocketbookapi.entity.Content;
import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.entity.Review;
import com.nbcamp.mypocketbookapi.repository.ContentJpaRepository;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import com.nbcamp.mypocketbookapi.repository.ReviewJpaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewJpaRepository reviewRepository;
	private final MemberJpaRepository memberRepository;
	private final ContentJpaRepository contentRepository;

	// 리뷰 작성
	@Transactional
	public ReviewResponseDto createReview(Long memberId, Long contentId, ReviewRequestDto requestDto ) {

		// memberRepository.findById(memberId)
		// SELECT m.* FROM member m WHERE m.id = ?
		Member member = memberRepository.findById(memberId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 회원입니다")
		);

		// contentRepository.findById(contentId)
		// SELECT c.* FROM content c WHERE c.id = ?
		Content content = contentRepository.findById(contentId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 콘텐츠입니다")
		);

		Review review = Review.builder()
			.member(member)
			.content(content)
			.rating(requestDto.getRating())
			.text(requestDto.getText())
			.build();

		// reviewRepository.save(review)
		// INSERT INTO review (member_id, content_id, rating, text) VALUES (?, ?, ?, ?)
		Review savedReview = reviewRepository.save(review);

		return new ReviewResponseDto(savedReview);
	}

	// ISBN 기준으로 모든 리뷰 조회 (모든 사용자가 해당 책에 등록한 리뷰)
	@Transactional
	public List<ReviewResponseDto> getReviewsByIsbn(String isbn) {
		// reviewRepository.findByContent_Isbn(isbn)
		// SELECT r.* FROM review r JOIN content c ON r.content_id = c.id WHERE c.isbn = ?
		List<Review> reviews = reviewRepository.findByContent_Isbn(isbn);

		if(reviews.isEmpty()) {
			throw new RuntimeException("해당 ISBN에 대한 리뷰가 존재하지 않습니다");
		}

		return reviews.stream()
			.map(ReviewResponseDto::new)
			.collect(Collectors.toList());
	}

	// 전체 리뷰 조회 (페이징 기능 추가 및 N+1 문제 개선)
	@Transactional
	public Page<ReviewResponseDto> getAllReviews(Pageable pageable) {
		// reviewRepository.findAllWithMemberAndContent(pageable)
		// SELECT r FROM Review r JOIN FETCH r.member JOIN FETCH r.content
		Page<Review> reviewsPage = reviewRepository.findAllWithMemberAndContent(pageable);

		return reviewsPage.map(ReviewResponseDto::new);
	}


	// 특정 콘텐츠의 특정 리뷰 단건 조회
	@Transactional
	public ReviewResponseDto getReviewByContentIdAndReviewId(Long contentId, Long reviewId) {

		// contentRepository.findById(contentId)
		// SELECT c.* FROM content c WHERE c.id = ?
		contentRepository.findById(contentId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 콘텐츠입니다")
		);

		// reviewRepository.findByContentIdAndId(contentId, reviewId)
		// SELECT r.* FROM review r WHERE r.content_id = ? AND r.id = ?
		Review review = reviewRepository.findByContentIdAndId(contentId, reviewId);

		if (review == null) {
			throw new RuntimeException("해당 콘텐츠에서 리뷰를 찾을 수 없습니다");
		}

		return new ReviewResponseDto(review);
	}

	// 리뷰 수정
	@Transactional
	public ReviewResponseDto updateReview(Long memberId, Long contentId, Long reviewId, ReviewRequestDto requestDto) {

		// memberRepository.findById(memberId)
		// SELECT m.* FROM member m WHERE m.id = ?
		// 회원 존재 확인
		memberRepository.findById(memberId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 회원입니다")
		);

		// contentRepository.findById(contentId)
		// SELECT c.* FROM content c WHERE c id = ?
		// 콘텐츠 존재 확인
		contentRepository.findById(contentId).orElseThrow(
			()-> new RuntimeException("존재하지 않는 콘텐츠입니다")
		);

		// reviewRepository.findByContentIdAndId(contentId, reviewId)
		// SELECT r. FROM review r WHERE r.content_id = ? AND r.id = ?
		// 리뷰 조회 및 작성자 확인
		Review review = reviewRepository.findByContentIdAndId(contentId, reviewId);
		if (review == null) {
			throw new RuntimeException("해당 콘텐츠에서 리뷰를 찾을 수 없습니다");
		}

		if (!review.getMember().getId().equals(memberId)) {
			throw new RuntimeException("리뷰 작성자만 수정할 수 있습니다");
		}

		// review.updateReview(...)  메서드 호출 후 @Transactional에 의해 변경 감지
		// UPDATE review SET rating = ?, text =? where id = ?
		// 리뷰 수정
		review.updateReview(requestDto.getRating(), requestDto.getText());

		return new ReviewResponseDto(review);
	}

	// 리뷰 삭제
	@Transactional
	public void deleteReview(Long memberId, Long reviewId) {

		// memberRepository.findById(memberId)
		// SELECT m.* FROM member m WHERE m.id = ?
		// 회원 존재 확인
		memberRepository.findById(memberId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 회원입니다")
		);

		// reviewRepository.findById(reviewId)
		// SELECT r.* FROM review r WHERE r.id = ?
		// 리뷰 존재 확인
		Review review = reviewRepository.findById(reviewId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 리뷰 입니다")
		);


		// 작성자 확인
		if(!review.getMember().getId().equals(memberId)) {
			throw new RuntimeException("리뷰 작성자만 삭제할 수 있습니다");
		}

		// reviewRepository.delete(review)
		// DELETE FROM review WHERE id = ?
		reviewRepository.delete(review);
	}

}
