package com.nbcamp.mypocketbookapi.dto.wishlist;

import lombok.Getter;

@Getter
public class WishlistRequestDto {

    private Long contentId;
    private Long memberId;
    private String isbn;


    public WishlistRequestDto(Long contentId, Long memberId, String isbn) {
        this.contentId = contentId;
        this.memberId = memberId;
        this.isbn = isbn;

    }
}
