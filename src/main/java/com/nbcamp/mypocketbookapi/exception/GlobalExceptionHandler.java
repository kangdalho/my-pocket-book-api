package com.nbcamp.mypocketbookapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
//예외 전역처리 어노테이션
@RestControllerAdvice
public class GlobalExceptionHandler {
    // BusinessException 발생 시 공통적으로 처리하는 예외 핸들러
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        // BusinessException 안에 정의된 ErrorCode를 꺼낸다.
        ErrorCode errorCode = ex.getErrorCode();
        // 에러 코드와 메시지를 포함한 ErrorResponse 객체 생성
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
        // HTTP 상태 코드와 함께 ErrorResponse를 응답으로 반환
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }
//    // 이미 사용중인 이메일로 회원가입 할 때 던지는 커스텀 예외처리
//    @ExceptionHandler(EmailAlreadyExistsException.class)
//    public ResponseEntity<?> handleMemberNotFound(EmailAlreadyExistsException ex) {
//        // 400 Bad Request로 처리 (로그인 실패 사유 전달)
//        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
//    }
//    // 이미 사용중인 닉네임으로 회원가입 할 때 던지는 커스텀 예외처리
//    @ExceptionHandler(NicknameAlreadyExistsException.class)
//    public ResponseEntity<?> handleMemberNotFound(NicknameAlreadyExistsException ex) {
//        // 400 Bad Request로 처리 (로그인 실패 사유 전달)
//        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
//    }
//    // 비밀번호가 일치하지 않을 때 던지는 커스텀 예외처리
//    @ExceptionHandler(PasswordMismatchException.class)
//    public ResponseEntity<?> handleMemberNotFound(PasswordMismatchException ex) {
//        // 400 Bad Request로 처리 (로그인 실패 사유 전달)
//        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
//    }
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
