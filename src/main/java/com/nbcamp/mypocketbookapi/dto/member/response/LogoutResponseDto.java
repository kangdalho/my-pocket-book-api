package com.nbcamp.mypocketbookapi.dto.member.response;


import lombok.Getter;

@Getter
public class LogoutResponseDto {
    private final String message;

    public LogoutResponseDto(String message) {
        this.message = message;
    }
}
