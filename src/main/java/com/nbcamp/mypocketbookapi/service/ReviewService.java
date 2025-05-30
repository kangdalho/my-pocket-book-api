package com.nbcamp.mypocketbookapi.service;

import org.springframework.stereotype.Service;

import com.nbcamp.mypocketbookapi.dto.ReviewRequestDto;
import com.nbcamp.mypocketbookapi.dto.ReviewResponseDto;
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

		// 멤버
		Member member = memberRepository.findById(memberId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 회원입니다")
		);

		// 콘텐츠
		Content content =contentRepository.findById(contentId).orElseThrow(
			() -> new RuntimeException("존재하지 않는 콘텐츠입니다")
		);

		// Review 엔티티 빌더
		Review review = Review.builder()
			.member(member)
			.content(content)
			.rating(requestDto.getRating())
			.text(requestDto.getText())
			.build();

		// Review 저장
		Review savedReview = reviewRepository.save(review);

		// ResponseDto 로 변환하여 반환
		return new ReviewResponseDto(savedReview);
	}

}
