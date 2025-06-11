package com.nbcamp.mypocketbookapi.repository;

import java.util.List;
import java.util.Optional;

import com.nbcamp.mypocketbookapi.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

	// 특정 콘텐츠의 모든 리뷰 조회
	// SELECT r.* FROM review r WHERE r.content_id = ?
	List<Review> findByContentId(Long contentId);

	// 특정 콘텐츠의 특정 리뷰 단건 조회
	// SELECT r.* FROM review r WHERE r.content_id = ? AND r.id = ?
	Review findByContentIdAndId(Long contentId, Long reviewId);

	// ISBN 기준으로 모든 리뷰 조회 (페이징 기능 및 N+1 문제 개선)
	// Review, Member, Content 엔티티를 JOIN FETCH 하여 N+1 문제를 방지합니다.
	// 또한, 주어진 ISBN에 해당하는 리뷰만 필터링하고 페이징을 적용합니다.
	@Query(value = "SELECT r FROM Review r JOIN FETCH r.member JOIN FETCH r.content WHERE r.content.isbn = :isbn",
		countQuery = "SELECT COUNT(r) FROM Review r JOIN r.content c WHERE c.isbn = :isbn")
	Page<Review> findByContent_IsbnWithMemberAndContent(String isbn, Pageable pageable);


	// JPQL을 사용하여 Review, Member, Content를 함께 조회 (N+1 문제 해결)
	// 페이징 처리된 모든 리뷰를 조회하며, Member와 Content 정보를 즉시 로딩하여 N+1 문제를 방지합니다.
	@Query(value = "SELECT r FROM Review r JOIN FETCH r.member JOIN FETCH r.content",
		countQuery = "SELECT COUNT(r) FROM Review r") // 페이징을 위한 전체 개수 쿼리를 명시
	Page<Review> findAllWithMemberAndContent(Pageable pageable);
}