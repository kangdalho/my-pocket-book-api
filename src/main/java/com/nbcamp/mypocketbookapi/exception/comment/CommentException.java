package com.nbcamp.mypocketbookapi.exception.comment;

import com.nbcamp.mypocketbookapi.exception.BusinessException;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;

public class CommentException extends BusinessException {
    public CommentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
