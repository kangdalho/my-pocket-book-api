package com.nbcamp.mypocketbookapi.service;

import com.nbcamp.mypocketbookapi.dto.wishlist.WishlistResponseDto;
import com.nbcamp.mypocketbookapi.entity.Content;
import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.entity.Wishlist;
import com.nbcamp.mypocketbookapi.repository.ContentJpaRepository;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import com.nbcamp.mypocketbookapi.repository.WishlistJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WishlistService {

    private final ContentJpaRepository contentRepository;
    private final MemberJpaRepository memberRepository;
    private final WishlistJpaRepository wishlistRepository;

    public WishlistResponseDto saveWishlist(Long contentId, Long memberId, String isbn) {

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 콘텐츠입니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Wishlist wishlist = Wishlist.create(content, member, isbn);
        Wishlist saved = wishlistRepository.save(wishlist);

        return WishlistResponseDto.builder()
                .wishlistId(saved.getId())
                .contentId(content.getId())
                .build();
    }

    public List<WishlistResponseDto> getWishlist(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        List<Wishlist> wishlistList = wishlistRepository.findByMember(member);

        return wishlistList.stream()
                .map(wishlist -> WishlistResponseDto.builder()
                        .wishlistId(wishlist.getId())
                        .contentId(wishlist.getContent().getId())
                        .memberId(wishlist.getMember().getId())
                        .isbn(wishlist.getIsbn())
                        .title(wishlist.getContent().getTitle())
                        .thumbnail(wishlist.getContent().getThumbnail())
                        .bookLink(wishlist.getContent().getBookLink())
                        .summary(wishlist.getContent().getSummary())
                        .salePrice(wishlist.getContent().getSalePrice())
                        .status(wishlist.getContent().getStatus())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
