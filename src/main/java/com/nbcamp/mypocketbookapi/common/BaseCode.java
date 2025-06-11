package com.nbcamp.mypocketbookapi.common;

import org.springframework.http.HttpStatus;

public interface BaseCode {
    DomainType getDomainType();
    HttpStatus getHttpStatus();
    String getMessage();
}
