package com.nbcamp.mypocketbookapi.exception;

import com.nbcamp.mypocketbookapi.common.BaseCode;
import com.nbcamp.mypocketbookapi.common.DomainType;
import org.springframework.http.HttpStatus;

public enum ErrorCode implements BaseCode {
    // Auth
    UNAUTHORIZED(DomainType.AUTH, HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    // Member
    EMAIL_NOT_REGISTERED(DomainType.MEMBER, HttpStatus.UNAUTHORIZED, "등록되지 않은 이메일입니다. 회원가입 후 로그인해주세요."),
    EMAIL_ALREADY_EXISTS(DomainType.MEMBER, HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(DomainType.MEMBER, HttpStatus.CONFLICT, "이미 사용중인 닉네임입니다."),
    EMAIL_MISMATCH(DomainType.MEMBER, HttpStatus.UNAUTHORIZED, "이메일이 일치하지 않습니다."),
    PASSWORD_MISMATCH(DomainType.MEMBER, HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    MEMBER_NOT_FOUND(DomainType.MEMBER, HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),


    // Content
    CONTENT_NOT_FOUND(DomainType.CONTENT, HttpStatus.NOT_FOUND, "콘텐츠(도서)가 존재하지 않습니다."),
    UNAUTHORIZED_CONTENT_DELETION(DomainType.CONTENT, HttpStatus.FORBIDDEN, "작성자만 삭제 할수있습니다."),
    DUPLICATE_CONTENT(DomainType.CONTENT, HttpStatus.CONFLICT, "이미 등록된 도서입니다."),


    // Review
    REVIEW_NOT_FOUND(DomainType.REVIEW, HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
    UNAUTHORIZED_REVIEW_MODIFICATION(DomainType.REVIEW, HttpStatus.FORBIDDEN, "리뷰는 작성자만 수정할 수 있습니다."),
    UNAUTHORIZED_REVIEW_DELETION(DomainType.REVIEW, HttpStatus.FORBIDDEN, "리뷰는 작성자만 삭제할 수 있습니다."),


    // Comment
    UNAUTHORIZED_COMMENT_MODIFICATION(DomainType.COMMENT, HttpStatus.FORBIDDEN, "댓글은 작성자만 수정할 수 있습니다."),
    UNAUTHORIZED_COMMENT_DELETION(DomainType.COMMENT, HttpStatus.FORBIDDEN, "댓글은 작성자만 삭제할 수 있습니다.");


    // ReviewLike



    // Wishlist


    private final DomainType domainType;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(DomainType domainType, HttpStatus httpStatus, String message) {
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