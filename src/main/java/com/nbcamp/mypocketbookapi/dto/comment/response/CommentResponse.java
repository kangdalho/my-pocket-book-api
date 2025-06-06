package com.nbcamp.mypocketbookapi.dto.comment.response;

import com.nbcamp.mypocketbookapi.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponse {

    private Long id;
    private String text;
    private Long memberId;
    private Long reviewId;

    @Builder
    public CommentResponse(Long id, String text, Long memberid, Long reviewId) {
        this.id = id;
        this.text = text;
        this.memberId = memberId;
        this.reviewId = reviewId;

    }

    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .memberid(comment.getMember().getId())
                .build();
    }
}