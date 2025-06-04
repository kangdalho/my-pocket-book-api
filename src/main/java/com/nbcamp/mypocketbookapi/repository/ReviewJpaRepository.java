package com.nbcamp.mypocketbookapi.repository;

import java.util.List;
import java.util.Optional;

import com.nbcamp.mypocketbookapi.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

	// 특정 콘텐츠의 모든 리뷰 조회
	List<Review> findByContentId(Long contentId);

	// 특정 콘텐츠의 특정 리뷰 단건 조회
	Review findByContentIdAndId(Long contentId, Long reviewId);



}
