package com.nbcamp.mypocketbookapi.service;

import com.nbcamp.mypocketbookapi.dto.ReviewLikeResponseDto;
import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.entity.Review;
import com.nbcamp.mypocketbookapi.entity.ReviewLike;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import com.nbcamp.mypocketbookapi.repository.ReviewJpaRepository;
import com.nbcamp.mypocketbookapi.repository.ReviewLikeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ReviewLikeService {

    private final MemberJpaRepository memberRepository;
    private final ReviewJpaRepository reviewRepository;
    private final ReviewLikeJpaRepository reviewLikeRepository;

    // 리뷰 좋아요 구현
    public void reviewLike (Long memberId, Long reviewId) {
        Member member = memberRepository.findById(memberId).orElseThrow( 
                () -> new RuntimeException("해당 회원을 찾을수 없습니다.")
        );
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new RuntimeException("해당 리뷰을 찾을수 없습니다.")
        );
        ReviewLike reviewLike = new ReviewLike(member, review);

        reviewLikeRepository.save(reviewLike);
 }
    // 리뷰 좋아요 취소 구현
    public void deleteReviewLike (Long memberId, Long reviewId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new RuntimeException("해당 회원을 찾을수 없습니다.")
        );
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new RuntimeException("해당 리뷰을 찾을수 없습니다.")
        );
        ReviewLike reviewLike  = reviewLikeRepository.findByReviewAndMember(review, member).orElseThrow(
                () -> new RuntimeException("해당 리뷰을 찾을수 없습니다.")
        );

        reviewLikeRepository.delete(reviewLike);

}

    // 리뷰 좋아요 전체 조회 구현
    public List<ReviewLikeResponseDto> checkAllReviewLikes (Long memberId) { // 이 메서드는 외부에서 호출 가능한 public 메서드이며, Long 타입의 memberId(회원 ID)를 매개변수로 받아 해당 회원이 좋아요한 리뷰들을 ReviewLikeResponseDto 형태로 리스트에 담아 반환한다.
        Member member = memberRepository.findById(memberId).orElseThrow( // memberRepository에서 주어진 memberId로 회원을 조회하고, 조회 결과가 없으면 "해당 회원을 찾을 수 없습니다."라는 예외(RuntimeException)를 발생시킨다. 조회된 회원 객체는 = 연산자를 통해 오른쪽 결과값을 왼쪽 변수 member에 저장(할당)한다.
                () -> new RuntimeException("해당 회원을 찾을수 없습니다."));

        List<ReviewLikeResponseDto> responseDtos = new ArrayList<>(); // ReviewLikeResponseDto 타입의 요소들을 담을 수 있는 List(리스트) 인터페이스 타입의 변수 responseDtos를 선언하고, = 연산자를 사용해 실제로 데이터를 담을 수 있도록 ArrayList 객체를 생성해서 저장한다.
        List<ReviewLike> reviewLikes =  reviewLikeRepository.findByMemeber(member); // reviewLikeRepository에서 해당 member가 좋아요한 리뷰 목록을 조회하고, 그 결과(List<ReviewLike>)를 reviewLikes 변수에 = 연산자를 이용해 저장한다.
        for(ReviewLike reviewLike : reviewLikes){
            Review review = reviewLike.getReview();
            ReviewLikeResponseDto reviewLikeResponseDto = new ReviewLikeResponseDto(
                    review.getId(), review.getContent().getId(), review.getText(),review.getRating()
            );
            responseDtos.add(reviewLikeResponseDto);
        }

        return responseDtos;
    }

}
