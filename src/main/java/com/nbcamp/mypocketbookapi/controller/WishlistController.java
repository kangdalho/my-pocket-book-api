package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.common.BaseResponse;
import com.nbcamp.mypocketbookapi.common.LoginMember;
import com.nbcamp.mypocketbookapi.common.ResponseCode;
import com.nbcamp.mypocketbookapi.dto.wishlist.WishlistResponseDto;
import com.nbcamp.mypocketbookapi.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/api/contents/{contentId}/wishlists")
    public ResponseEntity<BaseResponse<WishlistResponseDto>> saveWishlist(@LoginMember Long memberId,
                                                                          @PathVariable Long contentId,
                                                                          @RequestParam String isbn) {

        WishlistResponseDto wishlistResponseDto = wishlistService.saveWishlist(contentId, memberId, isbn);

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success(ResponseCode.SUCCESS_CREATED, wishlistResponseDto));
    }

    @GetMapping("/api/wishlists")
    public ResponseEntity<BaseResponse<List<WishlistResponseDto>>> listAll(@LoginMember Long memberId) {

        List<WishlistResponseDto> wishlists = wishlistService.getWishlist(memberId);

        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_OK, wishlists));
    }

    @DeleteMapping("/api/wishlists/{wishlistId}")
    public ResponseEntity<BaseResponse<Void>> deleteByWish(@PathVariable Long wishlistId) {

        wishlistService.deleteByWish(wishlistId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(BaseResponse.success(ResponseCode.SUCCESS_WISHLIST_DELETED));
    }
}
