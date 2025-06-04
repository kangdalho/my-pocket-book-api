package com.nbcamp.mypocketbookapi.service;

import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.entity.Review;
import com.nbcamp.mypocketbookapi.entity.ReviewLike;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import com.nbcamp.mypocketbookapi.repository.ReviewJpaRepository;
import com.nbcamp.mypocketbookapi.repository.ReviewLikeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ReviewLikeService {

    private final MemberJpaRepository memberRepository;
    private final ReviewJpaRepository reviewRepository;
    private final ReviewLikeJpaRepository reviewLikeRepository;

    // 리뷰 좋아요 구현
    public void reviewLike (Long memberId, Long reviewId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new RuntimeException("해당 회원을 찾을수 없습니다.")
        );
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new RuntimeException("해당 리뷰을 찾을수 없습니다.")
        );
        ReviewLike reviewLike = new ReviewLike(member, review);

        reviewLikeRepository.save(reviewLike);
 }
    // 리뷰 좋아요 취소 구현
    public void deleteReviewLike (Long memberId, Long reviewId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new RuntimeException("해당 회원을 찾을수 없습니다.")
        );
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new RuntimeException("해당 리뷰을 찾을수 없습니다.")
        );
        ReviewLike reviewLike  = reviewLikeRepository.findByReviewAndMember(review, member).orElseThrow(
                () -> new RuntimeException("해당 리뷰을 찾을수 없습니다.")
        );

        reviewLikeRepository.delete(reviewLike);

}

}
