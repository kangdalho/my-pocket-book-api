package com.nbcamp.mypocketbookapi.dto.wishlist;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WishlistResponseDto {

    private Long wishlistId;
    private Long contentId;
    private Long memberId;
    private String isbn;
    private String title;
    private String thumbnail;
    private String bookLink;
    private String summary;
    private Integer salePrice;
    private String status;

    @Builder
    public WishlistResponseDto(Long wishlistId, Long contentId, Long memberId, String isbn, String thumbnail, String title, String bookLink, String summary, Integer salePrice, String status) {
        this.wishlistId = wishlistId;
        this.contentId = contentId;
        this.memberId = memberId;
        this.isbn = isbn;
        this.thumbnail = thumbnail;
        this.title = title;
        this.bookLink = bookLink;
        this.summary = summary;
        this.salePrice = salePrice;
        this.status = status;
    }
}
