package com.nbcamp.mypocketbookapi.repository;

import java.util.Optional;

import com.nbcamp.mypocketbookapi.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

	// 1. 콘텐츠 ID와 리뷰 ID로 특정 리뷰 조회 (Member, Content 즉시 로딩)
	@EntityGraph(attributePaths = {"member", "content"})
	Optional<Review> findByContentIdAndId(Long contentId, Long reviewId);

	// 2. ISBN으로 리뷰 페이징 조회 (Member, Content 즉시 로딩, N+1 문제 해결)
	@EntityGraph(attributePaths = {"member", "content"})
	Page<Review> findByContent_Isbn(String isbn, Pageable pageable);

	// 3. 모든 리뷰 페이징 조회 (Member, Content 즉시 로딩, N+1 문제 해결)
	@EntityGraph(attributePaths = {"member", "content"})
	@Override
	Page<Review> findAll(Pageable pageable);

	// 4. 리뷰 ID로 리뷰 조회 (작성자 확인을 위한 Member 즉시 로딩)
	@EntityGraph(attributePaths = {"member"})
	@Override
	Optional<Review> findById(Long reviewId);

	// 5. 좋아요 순으로 상위 리뷰 조회 (좋아요 수 포함) (좋아요탑텐레디스)
	// 이 쿼리는 집계 함수와 그룹핑을 포함하므로 @EntityGraph 적용이 어려움
	@Query("""
        SELECT r, COUNT(rl.id) as likeCount
        FROM Review r
        LEFT JOIN ReviewLike rl ON r.id = rl.review.id
        GROUP BY r.id
        ORDER BY likeCount DESC, r.createdAt DESC
        """)
	Page<Object[]> findTopReviewsWithLikeCount(Pageable pageable);
}