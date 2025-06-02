package com.nbcamp.mypocketbookapi.dto.member.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberResponseDto {

    private final Long id;
    private final String email;
    private final String nickname;
    private final LocalDateTime createdAt;

    public MemberResponseDto(Long id, String email, String nickname, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }
}
