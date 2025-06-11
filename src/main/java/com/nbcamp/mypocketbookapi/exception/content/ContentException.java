package com.nbcamp.mypocketbookapi.exception.content;

import com.nbcamp.mypocketbookapi.exception.BusinessException;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;

public class ContentException extends BusinessException{
    public ContentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
