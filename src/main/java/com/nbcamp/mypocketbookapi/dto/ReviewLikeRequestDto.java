package com.nbcamp.mypocketbookapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewLikeRequestDto {
    private Long memberId;
    private Long reviewId;
}
