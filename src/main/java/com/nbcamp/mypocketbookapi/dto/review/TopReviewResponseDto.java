package com.nbcamp.mypocketbookapi.dto.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nbcamp.mypocketbookapi.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TopReviewResponseDto {

	private final Long id;
	private final String nickname;
	private final Long contentId;
	private final Integer rating;
	private final String text;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime createdAt;
	private final Long likeCount; // 좋아요 수

	public TopReviewResponseDto(Review review, Long likeCount) {

		this.id = review.getId();
		this.nickname = review.getMember().getNickname();
		this.contentId = review.getContent().getId();
		this.rating = review.getRating();
		this.text = review.getText();
		this.createdAt = review.getCreatedAt();
		this.likeCount = likeCount;
	}
}