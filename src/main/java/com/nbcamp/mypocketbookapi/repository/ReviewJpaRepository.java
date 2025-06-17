package com.nbcamp.mypocketbookapi.repository;

import java.util.Optional;

import com.nbcamp.mypocketbookapi.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

	// 콘텐츠 ID와 리뷰 ID로 특정 리뷰 조회 (Member, Content 즉시 로딩)
	@Query("""
        SELECT r
        FROM Review r
        JOIN FETCH r.member
        JOIN FETCH r.content
        WHERE r.content.id = :contentId AND r.id = :reviewId
        """)
	Review findByContentIdAndIdWithMemberAndContent(@Param("contentId") Long contentId, @Param("reviewId") Long reviewId);

	// ISBN으로 리뷰 페이징 조회 (Member, Content 즉시 로딩, N+1 문제 해결)
	// 특정 ISBN을 가진 콘텐츠의 모든 리뷰를 페이징 처리하여 가져오는 기능
	@Query(value = """
        SELECT r
        FROM Review r
        JOIN FETCH r.member
        JOIN FETCH r.content
        WHERE r.content.isbn = :isbn
        """,
		countQuery = """
           SELECT COUNT(r)
           FROM Review r
           JOIN r.content c
           WHERE c.isbn = :isbn
           """)
	Page<Review> findByContent_IsbnWithMemberAndContent(String isbn, Pageable pageable);

	// 모든 리뷰 페이징 조회 (Member, Content 즉시 로딩, N+1 문제 해결)
	// 데이터베이스에 저장된 모든 리뷰를 페이징하여 조회
	@Query(value = """
        SELECT r
        FROM Review r
        JOIN FETCH r.member
        JOIN FETCH r.content
        """,
		countQuery = """
           SELECT COUNT(r)
           FROM Review r
           """)
	Page<Review> findAllWithMemberAndContent(Pageable pageable);

	// 리뷰 ID로 리뷰 조회 (작성자 확인을 위한 Member 즉시 로딩)
	// ReviewService의 deleteReveiw 메소드 보면 , 리뷰 삭제하기 전에 해당 리뷰를 작성한 사용자가 맞는지 권한을 확인하는 로직이 있다. 그거때문에 필요함
	// 참고사항!! @EntityGraph 를 활용하여 더깔끔하게 개선할수잇으므로 나중에 고려해볼것
	@Query("""
        SELECT r
        FROM Review r
        JOIN FETCH r.member
        WHERE r.id = :reviewId
        """)
	Optional<Review> findByIdWithMember(@Param("reviewId") Long reviewId);

	// 좋아요 순으로 상위 리뷰 조회 (좋아요 수 포함) (좋아요탑텐레디스)
	@Query("""
        SELECT r, COUNT(rl.id) as likeCount
        FROM Review r
        LEFT JOIN ReviewLike rl ON r.id = rl.review.id
        GROUP BY r.id
        ORDER BY likeCount DESC, r.createdAt DESC
        """)
	Page<Object[]> findTopReviewsWithLikeCount(Pageable pageable);
}