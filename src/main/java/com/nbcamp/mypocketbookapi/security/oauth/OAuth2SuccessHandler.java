package com.nbcamp.mypocketbookapi.security.oauth;

import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import com.nbcamp.mypocketbookapi.security.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인 성공 시 principal에서 CustomOAuth2User로 캐스팅
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        // 닉네임 추출 (CustomOAuth2User에서 getName()이 닉네임 반환하도록 구현함)
        String nickname = oAuth2User.getName();
        // 닉네임으로 사용자 정보 조회 및 id값 추출
        Member member = memberJpaRepository.findByNickname(nickname);
        Long memberId = member.getId();
        // JWT 토큰 생성 (nickname + id)
        String token = jwtUtil.createToken(nickname, memberId);

        log.info("Generated JWT Token: {}", token);

        response.sendRedirect("http://localhost:8080/swagger-ui/index.html");
    }
}
