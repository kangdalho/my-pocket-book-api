package com.nbcamp.mypocketbookapi.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbcamp.mypocketbookapi.dto.member.request.LoginRequestDto;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;
import com.nbcamp.mypocketbookapi.exception.member.MemberException;
import com.nbcamp.mypocketbookapi.security.core.CustomMemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public LoginFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        // URL 설정 (기본값은 /login)
        setFilterProcessesUrl("/api/members/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            //JSON 형태의 로그인 요청 데이터를 읽어서 LoginRequestDto 객체로 변환
            LoginRequestDto loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

            // nickname을 username으로 사용
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getNickname(), loginRequest.getPassword());

            return getAuthenticationManager().authenticate(authToken); // 모든 검증을 해줌
        } catch (IOException e) {
            throw new MemberException(ErrorCode.JSON_PARSE_ERROR);
        }
    }

    // 로그인 성공 시 실행 (JWT 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();

        //정보 추출
        Long memberId = customMemberDetails.getMemberId();
        String nickname = customMemberDetails.getUsername();

        String token = jwtUtil.createToken(nickname, memberId);

        //헤더에 토큰 추가
        response.addHeader("Authorization", token);
        response.setContentType("application/json;charset=UTF-8");
    }

    // 로그인 실패 시 실행
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(401);
    }
}
