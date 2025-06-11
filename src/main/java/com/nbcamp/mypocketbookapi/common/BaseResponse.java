package com.nbcamp.mypocketbookapi.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {

    private final boolean success;
    private final int status;
    private final String message;
    private final T data;


    @Builder
    public BaseResponse(boolean success, int status, String message, T data) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> BaseResponse<T> success(BaseCode code, T data) {
        return BaseResponse.<T>builder()
                .success(true)
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> success(BaseCode code) {
        return BaseResponse.<T>builder()
                .success(true)
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .build();
    }

    public static <T> BaseResponse<T> fail(BaseCode code) {
        return BaseResponse.<T>builder()
                .success(false)
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .build();
    }
}
