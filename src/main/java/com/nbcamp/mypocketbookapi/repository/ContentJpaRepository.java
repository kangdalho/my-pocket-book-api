package com.nbcamp.mypocketbookapi.repository;

import com.nbcamp.mypocketbookapi.entity.Content;
import com.nbcamp.mypocketbookapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContentJpaRepository extends JpaRepository<Content, Long> {

    List<Content> findByMember(Member member);

    boolean existsByIsbn(String isbn);

    Optional<Content> findByIdAndMemberId(Long contentId, Long memberId);
}
