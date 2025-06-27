package com.nbcamp.mypocketbookapi.security.oauth;


import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import com.nbcamp.mypocketbookapi.security.oauth.dto.GoogleResponse;
import com.nbcamp.mypocketbookapi.security.oauth.dto.KakaoResponse;
import com.nbcamp.mypocketbookapi.security.oauth.dto.MemberDto;
import com.nbcamp.mypocketbookapi.security.oauth.dto.OAuth2Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 부모 클래스의 loadUser() 호출 → provider(google/kakao)에서 사용자 정보 가져옴
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 어떤 소셜 로그인 제공자인지 추출
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // provider별로 응답 포맷 다르므로 추상화된 응답 객체 사용
        OAuth2Response oAuth2Response;
        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }

        // 응답에서 email 추출
        String email = oAuth2Response.getEmail();
        // email로 기존 회원 조회
        Optional<Member> optionalMember = memberJpaRepository.findByEmail(email);
        Member member;

        if (optionalMember.isPresent()) {
            // 기존 회원이라면 정보 업데이트
            member = optionalMember.get();
            member.updateInfo(email, member.getNickname());
        } else {
            // 신규 회원인 경우 닉네임은 provider+id 조합으로 생성
            String nickname = oAuth2Response.getProvider() + oAuth2Response.getProviderId();
            member = new Member(email, "", nickname); // 비밀번호 공백
        }

        memberJpaRepository.save(member);
        // OAuth2 인증 성공 시 사용자 정보를 반환
        return new CustomOAuth2User(
                new MemberDto(member.getId(), member.getEmail(), member.getNickname())
        );
    }
}
