package com.nbcamp.mypocketbookapi.dto.comment.request;

import lombok.Getter;

@Getter
public class CommentRequest {

    public Long memberId;
    public Long reviewId;
    public String text;

}


