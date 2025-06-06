package com.nbcamp.mypocketbookapi.service;

import com.nbcamp.mypocketbookapi.dto.comment.request.CommentRequest;
import com.nbcamp.mypocketbookapi.dto.comment.response.CommentResponse;
import com.nbcamp.mypocketbookapi.entity.Comment;
import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.entity.Review;
import com.nbcamp.mypocketbookapi.repository.CommentJpaRepository;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import com.nbcamp.mypocketbookapi.repository.ReviewJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentJpaRepository commentJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ReviewJpaRepository reviewJpaRepository;



    @Transactional
    public Comment createComment(CommentRequest request) {
        Member member = memberJpaRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Review review = reviewJpaRepository.findById(request.getReviewId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        Comment comment = Comment.builder()
                .member(member)
                .review(review)
                .text(request.getText())
                .build();

        return commentJpaRepository.save(comment);
    }
    public List<CommentResponse> getAllComments() {
        return commentJpaRepository.findAll().stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());
    }

}
