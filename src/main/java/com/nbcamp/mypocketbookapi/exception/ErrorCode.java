package com.nbcamp.mypocketbookapi.exception;


import org.springframework.http.HttpStatus;

public enum ErrorCode {
    EMAIL_ALREADY_EXISTS("M001", "이미 사용중인 이메일입니다.", HttpStatus.CONFLICT),
    MEMBER_NOT_FOUND("M002", "회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NICKNAME_ALREADY_EXISTS("M003", "이미 사용중인 닉네임입니다.", HttpStatus.CONFLICT),
    PASSWORD_MISMATCH("M004", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);

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