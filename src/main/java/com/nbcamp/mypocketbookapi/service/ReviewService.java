package com.nbcamp.mypocketbookapi.service;

import com.nbcamp.mypocketbookapi.dto.review.ReviewRequestDto;
import com.nbcamp.mypocketbookapi.dto.review.ReviewResponseDto;
import com.nbcamp.mypocketbookapi.dto.review.TopReviewResponseDto;
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
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewJpaRepository reviewRepository;
	private final MemberJpaRepository memberRepository;
	private final ContentJpaRepository contentRepository;

	/**
	 * 좋아요 수 기준으로 상위 10개의 리뷰를 조회
	 * - 캐싱 적용: Redis의 'top10Reviews' 캐시에 10분간 저장
	 * - 캐시 미존재 시 DB 조회 후 캐시에 저장
	 */
	@Cacheable(value = "top10Reviews", key = "'all'", cacheManager = "cacheManager")
	@Transactional(readOnly = true)
	public List<TopReviewResponseDto> getTop10LikedReviews() {
		Pageable topTen = PageRequest.of(0, 10);
		Page<Object[]> results = reviewRepository.findTopReviewsWithLikeCount(topTen);

		// 결과를 DTO로 매핑하여 반환
		return results.getContent().stream()
			.map(result -> {
				Review review = (Review) result[0];
				Long likeCount = (Long) result[1];
				return new TopReviewResponseDto(review, likeCount);
			})
			.collect(Collectors.toList());
	}

	/**
	 * 새로운 리뷰를 생성
	 * - 리뷰 생성 시 관련 Member, Content 유효성 검증 수행
	 * - 리뷰가 생성되면 top10 캐시를 무효화하여 최신 순위 반영
	 *
	 * @param memberId 작성자 ID
	 * @param contentId 리뷰 대상 콘텐츠 ID
	 * @param requestDto 리뷰 작성 요청 데이터
	 * @return 생성된 리뷰 DTO
	 */
	@Transactional
	@CacheEvict(value = "top10Reviews", allEntries = true)
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

	/**
	 * 기존 리뷰를 수정
	 * - 작성자 일치 여부를 검증하여 권한 확인
	 * - 수정 시 캐시를 무효화하여 데이터 일관성 유지
	 *
	 * @param memberId 요청자 ID
	 * @param contentId 콘텐츠 ID
	 * @param reviewId 수정 대상 리뷰 ID
	 * @param requestDto 수정할 내용
	 * @return 수정된 리뷰 DTO
	 */
	@Transactional
	@CacheEvict(value = "top10Reviews", allEntries = true)
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

	/**
	 * 리뷰를 삭제
	 * - 작성자와 요청자 일치 여부를 검증하여 권한 확인
	 * - 삭제 시 Top10 캐시 무효화
	 *
	 * @param memberId 요청자 ID
	 * @param reviewId 삭제할 리뷰 ID
	 */
	@Transactional
	@CacheEvict(value = "top10Reviews", allEntries = true)
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

	/**
	 * 특정 ISBN을 가진 콘텐츠의 리뷰들을 페이징하여 조회
	 * - 첫 페이지인데 결과가 없는 경우 REVIEW_NOT_FOUND 예외 발생
	 *
	 * @param isbn 콘텐츠 ISBN
	 * @param pageable 페이지 정보
	 * @return 페이지 단위의 리뷰 응답 DTO
	 */
	@Transactional(readOnly = true)
	public Page<ReviewResponseDto> getReviewsByIsbn(String isbn, Pageable pageable) {
		Page<Review> reviewsPage = reviewRepository.findByContent_IsbnWithMemberAndContent(isbn, pageable);

		if (reviewsPage.isEmpty() && pageable.getPageNumber() == 0) {
			throw new ReviewException(ErrorCode.REVIEW_NOT_FOUND);
		}

		return reviewsPage.map(ReviewResponseDto::new);
	}

	/**
	 * 전체 리뷰를 페이지 단위로 조회
	 *
	 * @param pageable 페이지 정보
	 * @return 리뷰 목록
	 */
	@Transactional(readOnly = true)
	public Page<ReviewResponseDto> getAllReviews(Pageable pageable) {
		Page<Review> reviewsPage = reviewRepository.findAllWithMemberAndContent(pageable);

		if (reviewsPage.isEmpty() && pageable.getPageNumber() == 0) {
			throw new ReviewException(ErrorCode.REVIEW_NOT_FOUND);
		}

		return reviewsPage.map(ReviewResponseDto::new);
	}

	/**
	 * 특정 콘텐츠에 대한 특정 리뷰 단건 조회
	 * - 존재하지 않을 경우 예외 발생
	 *
	 * @param contentId 콘텐츠 ID
	 * @param reviewId 리뷰 ID
	 * @return 리뷰 응답 DTO
	 */
	@Transactional(readOnly = true)
	public ReviewResponseDto getReviewByContentIdAndReviewId(Long contentId, Long reviewId) {
		Review review = reviewRepository.findByContentIdAndIdWithMemberAndContent(contentId, reviewId);

		if (review == null) {
			throw new ReviewException(ErrorCode.REVIEW_NOT_FOUND);
		}

		return new ReviewResponseDto(review);
	}
}