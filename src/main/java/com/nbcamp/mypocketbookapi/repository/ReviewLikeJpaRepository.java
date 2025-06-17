package com.nbcamp.mypocketbookapi.repository;

import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.entity.Review;
import com.nbcamp.mypocketbookapi.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeJpaRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByReviewAndMember(Review review, Member member);

    List<ReviewLike> findByMember(Member member);

    // 특정 리뷰에 대한 총 좋아요 수 계산
    Long countByReview(Review review);
}
