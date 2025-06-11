package com.nbcamp.mypocketbookapi.exception.member;

import com.nbcamp.mypocketbookapi.exception.BusinessException;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;

public class MemberException extends BusinessException {
    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
