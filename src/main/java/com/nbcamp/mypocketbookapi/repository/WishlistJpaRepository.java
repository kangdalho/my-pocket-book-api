package com.nbcamp.mypocketbookapi.repository;

import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistJpaRepository extends JpaRepository<Wishlist, Long> {

    @EntityGraph(attributePaths = {"member", "content"})
    Page<Wishlist> findByMember(Member member, Pageable pageable);

    boolean existsByMemberAndIsbn(Member member, String isbn);

}
