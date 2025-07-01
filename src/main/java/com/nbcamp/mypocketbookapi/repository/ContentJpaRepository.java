package com.nbcamp.mypocketbookapi.repository;

import com.nbcamp.mypocketbookapi.entity.Content;
import com.nbcamp.mypocketbookapi.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ContentJpaRepository extends JpaRepository<Content, Long> {

    // entity graph 사용으로 n+1 해결
//    @EntityGraph(attributePaths = {"member"})
    Page<Content> findByMember(Member member, Pageable pageable);

    Optional<Content> findByIdAndMemberId(Long contentId, Long memberId);

    boolean existsByMemberAndIsbn(Member member, String isbn);

    // ContainingIgnoreCase - Containing = 포함 여부(like) IgnoreCase = 대소문자 구분안함
    @Query(
            value = """
        SELECT * FROM contents
        WHERE member_id = :memberId
          AND MATCH(summary) AGAINST(:keyword IN BOOLEAN MODE)
          ORDER BY created_at DESC      
        """,
            countQuery = """
        SELECT COUNT(*) FROM contents
        WHERE member_id = :memberId
          AND MATCH(summary) AGAINST(:keyword IN BOOLEAN MODE)
        """,
            nativeQuery = true
    )
    Page<Content> searchBySummaryFullText(@Param("memberId") Long memberId,
                                          @Param("keyword") String keyword,
                                          Pageable pageable);
    // ContainingIgnoreCase - Containing = 포함 여부(like) IgnoreCase = 대소문자 구분안함
//    Page<Content> findByMemberAndSummaryContainingIgnoreCase(@NotNull Member member, @NotBlank String summary, Pageable pageable);
}
