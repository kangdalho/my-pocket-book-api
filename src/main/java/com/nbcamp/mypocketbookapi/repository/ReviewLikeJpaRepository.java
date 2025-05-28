package com.nbcamp.mypocketbookapi.repository;

import com.nbcamp.mypocketbookapi.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeJpaRepository extends JpaRepository<ReviewLike, Long> {
}
