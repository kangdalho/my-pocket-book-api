package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.dto.WishlistRequestDto;
import com.nbcamp.mypocketbookapi.dto.WishlistResponseDto;
import com.nbcamp.mypocketbookapi.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/api/content/{contentId}/wishlists")
    public ResponseEntity<WishlistResponseDto> saveWishlist(@PathVariable Long contentId){

        Long memberId = 1L;
        WishlistResponseDto wishlistResponseDto = wishlistService.saveWishlist(contentId, memberId);

        return ResponseEntity.status(HttpStatus.CREATED).body(wishlistResponseDto);
    }
}
