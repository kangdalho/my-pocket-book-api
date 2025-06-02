package com.nbcamp.mypocketbookapi.dto.member.response;


import lombok.Getter;

@Getter
public class MessageResponseDto {
    private final String message;

    public MessageResponseDto(String message) {
        this.message = message;
    }
}
