package com.nbcamp.mypocketbookapi.security.oauth.dto;


import java.util.Map;

public class GoogleResponse implements OAuth2Response {
    // google로부터 받은 사용자 정보 형태(json)를 Map으로 저장
    private final Map<String, Object> attribute;

    public GoogleResponse(Map<String, Object> attribute) {

        this.attribute = attribute;
    }

    @Override
    public String getProvider() {

        return "google"; // OAuth 제공자가 Google임을 반환
    }

    @Override
    public String getProviderId() {

        return attribute.get("sub").toString(); // Google 고유 사용자 ID인 "sub" 값 가져옴
    }

    @Override
    public String getEmail() {

        return attribute.get("email").toString(); // email 가져옴
    }

    @Override
    public String getNickname() {

        return attribute.get("name").toString(); // 사용자 이름(혹은 닉네임)을 반환
    }
}