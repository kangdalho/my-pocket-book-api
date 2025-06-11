package com.nbcamp.mypocketbookapi.common;

import org.springframework.http.HttpStatus;

public enum ResponseCode implements BaseCode {

    // 공통 응답
    SUCCESS_OK(DomainType.SYSTEM, HttpStatus.OK, "요청이 성공적으로 처리되었습니다."),
    SUCCESS_CREATED(DomainType.SYSTEM, HttpStatus.CREATED, "리소스가 성공적으로 생성되었습니다."),
    SUCCESS_NO_CONTENT(DomainType.SYSTEM, HttpStatus.NO_CONTENT, "내용이 없습니다."),

    FAIL_BAD_REQUEST(DomainType.SYSTEM, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    FAIL_UNAUTHORIZED(DomainType.SYSTEM, HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FAIL_NOT_FOUND(DomainType.SYSTEM, HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    FAIL_INTERNAL_SERVER_ERROR(DomainType.SYSTEM, HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),

    // Member
    SUCCESS_SIGNUP(DomainType.MEMBER, HttpStatus.CREATED, "회원가입이 완료되었습니다."),
    SUCCESS_LOGIN(DomainType.MEMBER, HttpStatus.OK, "로그인을 성공하였습니다."),
    SUCCESS_FIND_ME(DomainType.MEMBER, HttpStatus.OK,"사용자 정보를 조회하였습니다"),
    SUCCESS_LOGOUT(DomainType.MEMBER, HttpStatus.OK,"로그아웃 하였습니다."),
    SUCCESS_WITHDRAW(DomainType.MEMBER, HttpStatus.OK,"회원탈퇴 되었습니다."),
    // Content
    SUCCESS_BOOK_REGISTERED(DomainType.CONTENT, HttpStatus.CREATED, ""),

    // Review
    SUCCESS_REVIEW_REGISTERED(DomainType.REVIEW, HttpStatus.CREATED, ""),

    // Comment
    SUCCESS_COMMENT_REGISTERED(DomainType.COMMENT, HttpStatus.CREATED, ""),

    // ReviewLike
    SUCCESS_REVIEW_LIKE_REGISTERED(DomainType.REVIEW_LIKE, HttpStatus.CREATED, ""),

    // Wishlist
    SUCCESS_WISHLIST_REGISTERED(DomainType.WISHLIST, HttpStatus.CREATED, "")

    ;

    private final DomainType domainType;
    private final HttpStatus httpStatus;
    private final String message;

    ResponseCode(DomainType domainType, HttpStatus httpStatus, String message) {
        this.domainType = domainType;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public DomainType getDomainType() {
        return this.domainType;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
