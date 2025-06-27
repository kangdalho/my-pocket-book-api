package com.nbcamp.mypocketbookapi.security.oauth;

import com.nbcamp.mypocketbookapi.security.oauth.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final MemberDto memberDto;

    @Override
    public String getName() {
        return memberDto.getNickname();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "memberId", memberDto.getMemberId(),
                "email", memberDto.getEmail(),
                "nickname", memberDto.getNickname()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getEmail() {
        return memberDto.getEmail();
    }

    public Long getMemberId() {
        return memberDto.getMemberId();
    }
}
