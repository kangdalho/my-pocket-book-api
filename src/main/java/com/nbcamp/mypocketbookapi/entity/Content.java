package com.nbcamp.mypocketbookapi.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "contents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @NotNull
    private Member member;

    @Column(nullable = false, length = 255, unique = true)
    @NotBlank
    private String isbn;

    @Column(nullable = false, length = 255)
    @NotBlank
    private String title;

    @Column(length = 512)
    @NotBlank
    private String thumbnail;

    @Column(length = 512)
    @NotBlank
    private String bookLink;

//    @Lob // lob = large object 대용량 객체
    @NotBlank
    private String summary;

    @Column
    @NotNull
    private Integer salePrice;

    @Column(length = 10)
    @NotBlank
    private String status;

    // 생성자는 객체를 만들때도 쓰이지만 데이터를 초기화하는데도 사용된다


    public Content(Member member, String isbn, String title, String thumbnail, String bookLink, String summary, Integer salePrice, String status) {
        this.member = member;
        this.isbn = isbn;
        this.title = title;
        this.thumbnail = thumbnail;
        this.bookLink = bookLink;
        this.summary = summary;
        this.salePrice = salePrice;
        this.status = status;
    }



}
