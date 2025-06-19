package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.common.BaseResponse;
import com.nbcamp.mypocketbookapi.common.LoginMember;
import com.nbcamp.mypocketbookapi.common.ResponseCode;
import com.nbcamp.mypocketbookapi.dto.wishlist.WishlistResponseDto;
import com.nbcamp.mypocketbookapi.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "F. 위시리스트", description = "위시리스트 관련 API")
public class WishlistController {

    private final WishlistService wishlistService;

    @Operation(summary = "위시리스트에 찜 등록", description = "로그인한 사용자가 특정 콘텐츠를 위시리스트에 찜 등록합니다.")
    @PostMapping("/contents/{contentId}/wishlists")
    public ResponseEntity<BaseResponse<WishlistResponseDto>> saveWishlist(@LoginMember Long memberId, @PathVariable Long contentId) {

        WishlistResponseDto wishlistResponseDto = wishlistService.saveWishlist(contentId, memberId);

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success(ResponseCode.SUCCESS_CREATED, wishlistResponseDto));
    }

    @Operation(summary = "위시리스트 전체 조회", description = "위시리스트 전체 목록을 조회합니다.")
    @GetMapping("/wishlists")
    public ResponseEntity<BaseResponse<Page<WishlistResponseDto>>> listAll(@LoginMember Long memberId, @ParameterObject @PageableDefault(size = 10, page = 0) Pageable pageable) {

        Page<WishlistResponseDto> wishlists = wishlistService.getWishlist(memberId, pageable);

        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_OK, wishlists));
    }

    @Operation(summary = "위시리스트 삭제", description = "위시리스트에 등록된 콘텐츠를 삭제합니다.")
    @DeleteMapping("/wishlists/{wishlistId}")
    public ResponseEntity<BaseResponse<Void>> deleteByWish(@PathVariable Long wishlistId) {

        wishlistService.deleteByWish(wishlistId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(BaseResponse.success(ResponseCode.SUCCESS_WISHLIST_DELETED));
    }
}
