package com.nbcamp.mypocketbookapi.exception;


import org.springframework.http.HttpStatus;

public enum ErrorCode {
    EMAIL_ALREADY_EXISTS("M001", "이미 사용중인 이메일입니다.", HttpStatus.CONFLICT),
    MEMBER_NOT_FOUND("M002", "회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NICKNAME_ALREADY_EXISTS("M003", "이미 사용중인 닉네임입니다.", HttpStatus.CONFLICT),
    EMAIL_MISMATCH("M004", "이메일이 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    PASSWORD_MISMATCH("M005", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    CONTENT_NOT_FOUND("M006", "콘텐츠(도서)가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_CONTENT_DELETION("M007", "작성자만 삭제 할수있습니다.", HttpStatus.FORBIDDEN),
    DUPLICATE_CONTENT("M008", "이미 등록된 도서입니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}