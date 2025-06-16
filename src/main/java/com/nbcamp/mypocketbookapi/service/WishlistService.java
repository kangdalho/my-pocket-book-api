package com.nbcamp.mypocketbookapi.service;

import com.nbcamp.mypocketbookapi.dto.wishlist.WishlistResponseDto;
import com.nbcamp.mypocketbookapi.entity.Content;
import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.entity.Wishlist;
import com.nbcamp.mypocketbookapi.exception.BusinessException;
import com.nbcamp.mypocketbookapi.exception.wishlist.WishlistException;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;
import com.nbcamp.mypocketbookapi.repository.ContentJpaRepository;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import com.nbcamp.mypocketbookapi.repository.WishlistJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final ContentJpaRepository contentRepository;
    private final MemberJpaRepository memberRepository;
    private final WishlistJpaRepository wishlistRepository;

    public WishlistResponseDto saveWishlist(Long contentId, Long memberId) {

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new WishlistException(ErrorCode.CONTENT_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new WishlistException(ErrorCode.MEMBER_NOT_FOUND));

        if (wishlistRepository.existsByMemberAndIsbn(member,content.getIsbn())) {
            throw new WishlistException(ErrorCode.WISHLIST_ALREADY_EXITS);
        }

        Wishlist wishlist = Wishlist.create(content, member, content.getIsbn());
        Wishlist saved = wishlistRepository.save(wishlist);

        return WishlistResponseDto.builder()
                .wishlistId(saved.getId())
                .contentId(content.getId())
                .build();
    }

    public Page<WishlistResponseDto> getWishlist(Long memberId, @ParameterObject Pageable pageable) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new WishlistException(ErrorCode.MEMBER_NOT_FOUND));

        Page<Wishlist> wishlistPage = wishlistRepository.findByMember(member, pageable);

        return wishlistPage.map(wishlist -> WishlistResponseDto.builder()
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
                );
    }

    public void deleteByWish(Long id) {

        Wishlist wishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> new WishlistException(ErrorCode.CONTENT_NOT_FOUND));

        wishlistRepository.delete(wishlist);
    }
}
