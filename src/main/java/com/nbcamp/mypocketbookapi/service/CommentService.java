package com.nbcamp.mypocketbookapi.service;

import com.nbcamp.mypocketbookapi.dto.comment.request.CommentRequest;
import com.nbcamp.mypocketbookapi.dto.comment.response.CommentResponse;
import com.nbcamp.mypocketbookapi.entity.Comment;
import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.entity.Review;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;
import com.nbcamp.mypocketbookapi.exception.comment.CommentException;
import com.nbcamp.mypocketbookapi.repository.CommentJpaRepository;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import com.nbcamp.mypocketbookapi.repository.ReviewJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentJpaRepository commentJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ReviewJpaRepository reviewJpaRepository;



    @Transactional
    public Comment createComment(Long reviewId, Long memberId, CommentRequest request) {
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new CommentException(ErrorCode.MEMBER_NOT_FOUND));
        Review review = reviewJpaRepository.findById(reviewId)
                .orElseThrow(() -> new CommentException(ErrorCode.REVIEW_NOT_FOUND));

        Comment comment = Comment.builder()
                .member(member)
                .review(review)
                .text(request.getText())
                .build();

        return commentJpaRepository.save(comment);
    }
    @Transactional
    public List<CommentResponse> getCommentsByReviewId(Long reviewId) {
        Review review = reviewJpaRepository.findById(reviewId)
                .orElseThrow(() -> new CommentException(ErrorCode.REVIEW_NOT_FOUND));

        List<Comment> comments = commentJpaRepository.findByReview(review);

        return comments.stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateComment(Long commentId, Long memberId, String newText) {
        Comment comment = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.CONTENT_NOT_FOUND));
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new CommentException(ErrorCode.MEMBER_NOT_FOUND));
        if(!Objects.equals(comment.getMember().getId(), member.getId())) {
            throw new CommentException(ErrorCode.UNAUTHORIZED_REVIEW_MODIFICATION);
        }
        comment.updateText(newText);
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.CONTENT_NOT_FOUND));
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new CommentException(ErrorCode.MEMBER_NOT_FOUND));
        if(!Objects.equals(comment.getMember().getId(), member.getId())) {
            throw new CommentException(ErrorCode.UNAUTHORIZED_COMMENT_DELETION);
        }
        commentJpaRepository.delete(comment);
    }
}
