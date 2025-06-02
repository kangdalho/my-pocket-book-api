package com.nbcamp.mypocketbookapi.repository;

import com.nbcamp.mypocketbookapi.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistJpaRepository extends JpaRepository<Wishlist, Long> {
}
