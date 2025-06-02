package com.nbcamp.mypocketbookapi.dto.member.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LoginResponseDto {

    private final Long id;
    private final String email;
    private final String nickname;

    public LoginResponseDto(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
}