package com.nbcamp.mypocketbookapi.repository;

import java.util.Optional;

import com.nbcamp.mypocketbookapi.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

	// 콘텐츠 ID와 리뷰 ID로 특정 리뷰 조회 (Member, Content 즉시 로딩)
	@Query("SELECT r FROM Review r JOIN FETCH r.member JOIN FETCH r.content WHERE r.content.id = :contentId AND r.id = :reviewId")
	Review findByContentIdAndIdWithMemberAndContent(@Param("contentId") Long contentId, @Param("reviewId") Long reviewId);

	// ISBN으로 리뷰 페이징 조회 (Member, Content 즉시 로딩, N+1 문제 해결)
	@Query(value = "SELECT r FROM Review r JOIN FETCH r.member JOIN FETCH r.content WHERE r.content.isbn = :isbn",
		countQuery = "SELECT COUNT(r) FROM Review r JOIN r.content c WHERE c.isbn = :isbn")
	Page<Review> findByContent_IsbnWithMemberAndContent(String isbn, Pageable pageable);

	// 모든 리뷰 페이징 조회 (Member, Content 즉시 로딩, N+1 문제 해결)
	@Query(value = "SELECT r FROM Review r JOIN FETCH r.member JOIN FETCH r.content",
		countQuery = "SELECT COUNT(r) FROM Review r")
	Page<Review> findAllWithMemberAndContent(Pageable pageable);

	// 리뷰 ID로 리뷰 조회 (Member 즉시 로딩)
	@Query("SELECT r FROM Review r JOIN FETCH r.member WHERE r.id = :reviewId")
	Optional<Review> findByIdWithMember(@Param("reviewId") Long reviewId);
}
