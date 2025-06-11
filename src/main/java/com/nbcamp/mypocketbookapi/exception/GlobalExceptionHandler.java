package com.nbcamp.mypocketbookapi.exception;

import com.nbcamp.mypocketbookapi.common.BaseResponse;
import com.nbcamp.mypocketbookapi.common.DomainType;
import com.nbcamp.mypocketbookapi.exception.comment.CommentException;
import com.nbcamp.mypocketbookapi.exception.content.ContentException;
import com.nbcamp.mypocketbookapi.exception.member.MemberException;
import com.nbcamp.mypocketbookapi.exception.review.ReviewException;
import com.nbcamp.mypocketbookapi.exception.reviewlike.ReviewLikeException;
import com.nbcamp.mypocketbookapi.exception.wishlist.WishListException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
//예외 전역처리 어노테이션
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CommentException.class)
    public ResponseEntity<BaseResponse<?>> handleCommentException(CommentException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("[{}] {} - {}", DomainType.COMMENT, errorCode.getHttpStatus().value(), errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.fail(errorCode));
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<BaseResponse<?>> handleMemberException(MemberException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("[{}] {} - {}", DomainType.MEMBER, errorCode.getHttpStatus().value(), errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.fail(errorCode));
    }

    @ExceptionHandler(ContentException.class)
    public ResponseEntity<BaseResponse<?>> handleContentException(ContentException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("[{}] {} - {}", DomainType.CONTENT, errorCode.getHttpStatus().value(), errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.fail(errorCode));
    }

    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<BaseResponse<?>> handleReviewException(ReviewException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("[{}] {} - {}", DomainType.REVIEW, errorCode.getHttpStatus().value(), errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.fail(errorCode));
    }

    @ExceptionHandler(ReviewLikeException.class)
    public ResponseEntity<BaseResponse<?>> handleReviewLikeException(ReviewLikeException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("[{}] {} - {}", DomainType.REVIEW_LIKE, errorCode.getHttpStatus().value(), errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.fail(errorCode));
    }

    @ExceptionHandler(WishListException.class)
    public ResponseEntity<BaseResponse<?>> handleWishListException(WishListException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("[{}] {} - {}", DomainType.WISHLIST, errorCode.getHttpStatus().value(), errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.fail(errorCode));
    }

    // 도메인별 예외 핸들링 필요시 BusinessException Handler 상단에(순서중요함) 각각의 도메인 Exception Handler 작성
    // BusinessException 발생 시 공통적으로 처리하는 예외 핸들러
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<?>> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("[{}] {} - {}", ex.getErrorCode().getDomainType(), errorCode.getHttpStatus().value(), errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.fail(errorCode));
    }

    // @Valid 유효성 검증에 실패했을 때 발생하는 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        // 가장 첫 번째 필드 에러의 메시지를 추출해서 반환
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        // 400 Bad Request로 클라이언트에 메시지 전달
        return ResponseEntity.badRequest().body(Map.of("error", message));
    }
    // 예상하지 못한 런타임 예외(RuntimeException) 처리
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        // 500 Internal Server Error 응답과 함께 예외 메시지 전달
        return ResponseEntity.status(500).body(Map.of("error", "서버 에러: " + ex.getMessage()));
    }
}
