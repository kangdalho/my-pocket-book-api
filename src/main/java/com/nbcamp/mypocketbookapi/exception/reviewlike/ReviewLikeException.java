package com.nbcamp.mypocketbookapi.exception.reviewlike;

import com.nbcamp.mypocketbookapi.exception.BusinessException;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;

public class ReviewLikeException extends BusinessException {
    public ReviewLikeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
