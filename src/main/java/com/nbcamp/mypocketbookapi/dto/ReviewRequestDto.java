package com.nbcamp.mypocketbookapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {

	private Integer rating;
	private String text;

}
