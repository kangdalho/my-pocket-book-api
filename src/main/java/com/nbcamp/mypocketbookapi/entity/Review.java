package com.nbcamp.mypocketbookapi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    @Column
    private int rating;

    @Lob
    private String text;

    @Builder
    public Review(Member member, Content content, int rating, String text) {
        this.member = member;
        this.content = content;
        this.rating = rating;
        this.text = text;
    }

    // 리뷰 수정 메서드
    public void updateReview(int rating, String text) {
        this.rating = rating;
        this.text = text;
    }


}