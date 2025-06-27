package com.nbcamp.mypocketbookapi.security.oauth.dto;


import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    /*
    Map<String, Object> 구조를 사용하는 이유:
    OAuth 제공자의 응답이 JSON 구조이므로 JSON을 Java에서 다루기 위해 일반적으로
    Map으로 변환, 필요한 데이터를 꺼내서 사용
     */
    private final Map<String, Object> attributes;

    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao"; // OAuth 제공자 이름 kakao
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString(); // kakao 사용자 고유 ID (최상단 위치)
    }

    @Override
    public String getEmail() {
        // attribute 맵에서 "kakao_account" 키의 값을 꺼내고 Map 형태로 캐스팅
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        // kakao_account 맵에서 "email" 키의 값을 꺼내 문자열로 반환
        return kakaoAccount.get("email").toString();
    }

    @Override
    public String getNickname() {
        // attributes 맵에서 "kakao_account" 키의 값을 꺼내고 Map 형태로 캐스팅
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        // kakao_account 맵에서 "profile" 키의 값을 꺼내고 Map 형태로 캐스팅
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        // profile 맵에서 "nickname" 키의 값을 꺼내 문자열로 반환
        return profile.get("nickname").toString();
    }
}
