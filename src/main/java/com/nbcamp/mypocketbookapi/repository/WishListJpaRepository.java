package com.nbcamp.mypocketbookapi.repository;

import com.nbcamp.mypocketbookapi.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListJpaRepository extends JpaRepository<WishList, Long> {
}
