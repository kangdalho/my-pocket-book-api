package com.nbcamp.mypocketbookapi.dto;

import lombok.Getter;

@Getter
public class MemberResponseDto {

    private final Long id;
    private final String email;
    private final String nickname;

    public MemberResponseDto(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
}
