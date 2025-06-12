package com.nbcamp.mypocketbookapi.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {

	@Schema(description = "리뷰 별점 (1~5점)", example = "4", required = true)
	@NotNull(message = "별점은 필수 입력 값입니다.")
	@Min(value = 1, message = "별점은 1점 이상이어야 합니다.")
	@Max(value = 5, message = "별점은 5점 이하여야 합니다.")
	private Integer rating;

	@Schema(description = "리뷰 내용 (1자 이상 500자 이하)", example = "이 책 정말 재미있었어요!", required = true)
	@NotNull(message = "리뷰 내용은 필수 입력 값입니다.")
	@Size(min = 1, max = 500, message = "리뷰 내용은 1자 이상 500자 이하여야 합니다.")
	private String text;
}
