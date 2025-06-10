package com.nbcamp.mypocketbookapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class ReviewLikeResponseDto {
    private Long reviewId;
    private Long contentId;
    private String text;
    private int rating;

}


