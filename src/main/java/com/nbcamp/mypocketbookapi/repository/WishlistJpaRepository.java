package com.nbcamp.mypocketbookapi.repository;

import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistJpaRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByMember(Member member);

    Optional<Wishlist> findById(Long id);
}
