package com.nbcamp.mypocketbookapi.repository;

import com.nbcamp.mypocketbookapi.entity.Content;
import com.nbcamp.mypocketbookapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentJpaRepository extends JpaRepository<Content, Long> {

    List<Content> findByMember(Member member);

    boolean existsByIsbn(String isbn);
}
