package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.dto.wishlist.WishlistResponseDto;
import com.nbcamp.mypocketbookapi.entity.Wishlist;
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
    public ResponseEntity<WishlistResponseDto> saveWishlist (@PathVariable Long contentId,
                                                             @RequestParam String isbn){

        Long memberId = 1L;
        WishlistResponseDto wishlistResponseDto = wishlistService.saveWishlist(contentId, memberId, isbn);

        return ResponseEntity.status(HttpStatus.CREATED).body(wishlistResponseDto);
    }

    @GetMapping("/api/wishlists")
    public ResponseEntity<List<WishlistResponseDto>> listAll(){

        Long memberId = 1L;
        List<WishlistResponseDto> wishlists = wishlistService.getWishlist(memberId);
        return ResponseEntity.ok(wishlists);
    }
}
