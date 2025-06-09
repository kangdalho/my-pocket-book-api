package com.nbcamp.mypocketbookapi.repository;

import com.nbcamp.mypocketbookapi.entity.Comment;
import com.nbcamp.mypocketbookapi.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByReview(Review review);
}
