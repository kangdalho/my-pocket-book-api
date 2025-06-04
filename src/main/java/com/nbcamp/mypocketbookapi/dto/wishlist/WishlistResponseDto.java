package com.nbcamp.mypocketbookapi.dto.wishlist;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WishlistResponseDto {

    private Long wishlistId;
    private Long contentId;
    private Long memberId;

    public WishlistResponseDto(Long wishlistId, Long contentId, Long memberId){
        this.wishlistId = wishlistId;
        this.contentId = contentId;
        this.memberId = memberId;
    }

}
