package com.nbcamp.mypocketbookapi.exception.review;

import com.nbcamp.mypocketbookapi.exception.BusinessException;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;

public class ReviewException extends BusinessException {
    public ReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}
