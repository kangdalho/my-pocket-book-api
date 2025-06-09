package com.nbcamp.mypocketbookapi.dto.comment.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    public Long memberId;
    public Long reviewId;
    public String text;

}


