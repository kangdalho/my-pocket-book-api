package com.nbcamp.mypocketbookapi.repository;

import com.nbcamp.mypocketbookapi.entity.Comment;
import com.nbcamp.mypocketbookapi.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByReview(Review review, Pageable pageable);
}
