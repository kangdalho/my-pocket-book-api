package com.nbcamp.mypocketbookapi.service;

import java.util.List;
import java.util.stream.Collectors;

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

		Member member = memberRepository.findById(memberId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 회원입니다")
		);

		Content content = contentRepository.findById(contentId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 콘텐츠입니다")
		);

		Review review = Review.builder()
			.member(member)
			.content(content)
			.rating(requestDto.getRating())
			.text(requestDto.getText())
			.build();

		Review savedReview = reviewRepository.save(review);

		return new ReviewResponseDto(savedReview);
	}

	// 특정 콘텐츠의 모든 리뷰 조회
	@Transactional
	public List<ReviewResponseDto> getReviewsByContentId(Long contentId) {
		contentRepository.findById(contentId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 콘텐츠입니다")
		);

		List<Review> reviews = reviewRepository.findByContentId(contentId);

		return reviews.stream()
			.map(ReviewResponseDto::new)
			.collect(Collectors.toList());
	}



	// 특정 콘텐츠의 특정 리뷰 단건 조회
	@Transactional
	public ReviewResponseDto getReviewByContentIdAndReviewId(Long contentId, Long reviewId) {

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
	@Transactional
	public ReviewResponseDto updateReview(Long memberId, Long contentId, Long reviewId, ReviewRequestDto requestDto) {

		// 회원 존재 확인
		memberRepository.findById(memberId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 회원입니다")
		);

		// 콘텐츠 존재 확인
		contentRepository.findById(contentId).orElseThrow(
			()-> new RuntimeException("존재하지 않는 회원입니다")
		);

		// 리뷰 조회 및 작성자 확인
		Review review = reviewRepository.findByContentIdAndId(contentId, reviewId);
		if (review == null) {
			throw new RuntimeException("해당 콘텐츠에서 리뷰를 찾을 수 없습니다");
		}

		if (!review.getMember().getId().equals(memberId)) {
			throw new RuntimeException("리뷰 작성자만 수정할 수 있습니다");
		}

		// 리뷰 수정
		review.updateReview(requestDto.getRating(), requestDto.getText());

		return new ReviewResponseDto(review);
	}


}
