package com.nbcamp.mypocketbookapi.dto.review;

import java.time.LocalDateTime;

import com.nbcamp.mypocketbookapi.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewResponseDto {

	private Long id;
	private String nickname; // 변경: memberId -> nickname
	private Long contentId;
	private Integer rating;
	private String text;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public ReviewResponseDto(Review review) {
		this.id = review.getId();
		this.nickname = review.getMember().getNickname(); // 변경: review.getMember().getId() -> review.getMember().getNickname()
		this.contentId = review.getContent().getId();
		this.rating = review.getRating();
		this.text = review.getText();
		this.createdAt = review.getCreatedAt();
		this.updatedAt = review.getUpdatedAt();
	}
}