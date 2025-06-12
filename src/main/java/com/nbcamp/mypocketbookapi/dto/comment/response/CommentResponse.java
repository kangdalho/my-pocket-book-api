package com.nbcamp.mypocketbookapi.dto.comment.response;

import com.nbcamp.mypocketbookapi.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponse {

    private Long id;
    private String text;
    private String nickname;
    private Long reviewId;

    @Builder
    public CommentResponse(Long id, String text, String nickname, Long reviewId) {

        this.id = id;
        this.text = text;
        this.nickname = nickname;
        this.reviewId = reviewId;
    }

    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .nickname(comment.getMember().getNickname())
                .reviewId(comment.getReview().getId())
                .build();
    }
}