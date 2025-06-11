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

	// ISBN 기준으로 모든 리뷰 조회 (Content 엔티티의 ISBN 필드를 조인하여 검색)
	// SELECT r.* FROM review r JOIN content c ON r.content_id = c.id WHERE c.isbn = ?
	List<Review> findByContent_Isbn(String isbn);

	// JPQL을 사용하여 Review, Member, Content를 함께 조회 (N+1 문제 해결)
	// 페이징 처리된 모든 리뷰 조회
	@Query(value = "SELECT r FROM Review r JOIN FETCH r.member JOIN FETCH r.content",
		countQuery = "SELECT COUNT(r) FROM Review r") // count 쿼리 명시
	Page<Review> findAllWithMemberAndContent(Pageable pageable);
}