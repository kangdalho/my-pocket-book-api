package com.nbcamp.mypocketbookapi.exception.wishlist;

import com.nbcamp.mypocketbookapi.exception.BusinessException;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;

public class WishlistException extends BusinessException {
    public WishlistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
