package com.nbcamp.mypocketbookapi.dto.reviewlike;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class ReviewLikeResponseDto {
    private Long reviewId;
    private String message;
}


