package com.nbcamp.mypocketbookapi.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponse {

    private Long id;
    private String text;
    private String memberId;
    private Long reviewId;

    @Builder
    public CommentResponse(Long id, String text, String memberName, Long reviewId) {
        this.id = id;
        this.text = text;
        this.memberId = memberId;
        this.reviewId = reviewId;

    }
}