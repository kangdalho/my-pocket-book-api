package com.nbcamp.mypocketbookapi.exception.wishlist;

import com.nbcamp.mypocketbookapi.exception.BusinessException;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;

public class WishListException extends BusinessException {
    public WishListException(ErrorCode errorCode) {
        super(errorCode);
    }
}
