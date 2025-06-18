package com.nbcamp.mypocketbookapi.repository;

import com.nbcamp.mypocketbookapi.entity.Content;
import com.nbcamp.mypocketbookapi.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContentJpaRepository extends JpaRepository<Content, Long> {

    // entity graph 사용으로 n+1 해결
//    @EntityGraph(attributePaths = {"member"})
    Page<Content> findByMember(Member member, Pageable pageable);

    Optional<Content> findByIdAndMemberId(Long contentId, Long memberId);

    boolean existsByMemberAndIsbn(Member member, String isbn);
}
