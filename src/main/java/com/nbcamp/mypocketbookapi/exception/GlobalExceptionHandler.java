package com.nbcamp.mypocketbookapi.exception;

import com.nbcamp.mypocketbookapi.common.BaseResponse;
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
    // BusinessException 발생 시 공통적으로 처리하는 예외 핸들러
    // 도메인별 예외 핸들링 필요시 BusinessException Handler 상단에 각각의 도메인 Exception Handler 작성
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<?>> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("[{}] {} - {}", ex.getErrorCode().getDomainType(), errorCode.getHttpStatus().value(), errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.fail(errorCode, errorCode.getMessage()));
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
