package com.nbcamp.mypocketbookapi.repository;

import java.util.List;
import java.util.Optional;

import com.nbcamp.mypocketbookapi.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

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



}
