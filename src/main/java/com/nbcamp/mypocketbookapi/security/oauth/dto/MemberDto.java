package com.nbcamp.mypocketbookapi.security.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class MemberDto {
    private Long memberId;
    private String email;
    private String nickname;
}
