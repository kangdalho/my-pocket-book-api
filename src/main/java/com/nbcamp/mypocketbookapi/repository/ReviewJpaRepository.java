package com.nbcamp.mypocketbookapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nbcamp.mypocketbookapi.entity.Review;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

	// N+1 문제 해결 : 특정 콘텐츠의 특정 리뷰 단건 조회 (상세 조회용)
	// 리뷰, 회원, 콘텐츠 정보를 함께 Fetch하여 N+1 문제 방지
	@Query("SELECT r FROM Review r JOIN FETCH r.member JOIN FETCH r.content WHERE r.content.id = :contentId AND r.id = :reviewId")
	Review findByContentIdAndIdWithMemberAndContent(@Param("contentId") Long contentId, @Param("reviewId") Long reviewId);

	// N+1 문제 해결 : 리뷰 수정/삭제 시 권한 확인을 위해 리뷰와 회원 정보를 함께 Fetch
	// 콘텐츠는 이 시점에 꼭 필요하지 않아 JOIN FETCH에서 제외
	@Query("SELECT r FROM Review r JOIN FETCH r.member WHERE r.content.id = :contentId AND r.id = :reviewId")
	Review findByContentIdAndIdWithMember(@Param("contentId") Long contentId, @Param("reviewId") Long reviewId);

	// N+1 문제 해결 : ISBN 기준으로 모든 리뷰 조회 (페이징 기능 및 Member, Content 정보 Fetch)
	@Query(value = "SELECT r FROM Review r JOIN FETCH r.member JOIN FETCH r.content c WHERE c.isbn = :isbn",
		countQuery = "SELECT COUNT(r.id) FROM Review r JOIN r.content c WHERE c.isbn = :isbn")
	Page<Review> findByContent_IsbnWithMemberAndContent(@Param("isbn") String isbn, Pageable pageable);

	// N+1 문제 해결 : 전체 리뷰 조회 (페이징 기능 및 Member, Content 정보 Fetch)
	@Query(value = "SELECT r FROM Review r JOIN FETCH r.member JOIN FETCH r.content",
		countQuery = "SELECT COUNT(r.id) FROM Review r")
	Page<Review> findAllWithMemberAndContent(Pageable pageable);

	// N+1 문제 해결 : 리뷰 삭제 시 권한 확인을 위해 리뷰와 회원 정보를 함께 Fetch (리뷰 ID만으로 조회)
	@Query("SELECT r FROM Review r JOIN FETCH r.member WHERE r.id = :reviewId")
	Review findByIdWithMember(@Param("reviewId") Long reviewId);
}