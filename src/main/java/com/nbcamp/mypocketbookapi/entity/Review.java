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

    // 리뷰 ID (기본 키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 리뷰 작성자 (Member 엔티티와 다대일 관계, 지연 로딩)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 리뷰 대상 콘텐츠 (Content 엔티티와 다대일 관계, 지연 로딩)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    // 리뷰 평점
    @Column
    private int rating;

    // 리뷰 내용 (대용량 텍스트)
    @Lob
    private String text;

    // 리뷰 생성자 (Builder 패턴 사용)
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
