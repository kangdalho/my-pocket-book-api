package com.nbcamp.mypocketbookapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization"; // HTTP Header 키 값
    public static final String BEARER_PREFIX = "Bearer ";
    private final long TOKEN_EXPIRATION_TIME = 1000L * 60 * 60; // 60분

    @Value("${jwt.secreat.key}")
    private String secretKey;
    private SecretKey key;
    private final MacAlgorithm algorithm = Jwts.SIG.HS256;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    // 토큰 생성 : 로그인 성공 시 발급
    public String createToken(String nickname, Long memberId) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .subject(nickname)
                        .claim("memberId", memberId)
                        .expiration(new Date(date.getTime() + TOKEN_EXPIRATION_TIME))
                        .issuedAt(date) // 발급일
                        .signWith(key, algorithm) // secreatKey
                        .compact();

    }

    // Header에서 Bearer 토큰을 가져와서 순수한 Token으로 반환
    public Optional<String> extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return Optional.of(bearerToken.substring(BEARER_PREFIX.length()));
        }
        return Optional.empty();
    }

    // 토큰 검증 : 인증이 필요한 요청이 들어왔을 때
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }

    // 토큰 사용자 정보 추출
    public Claims getClaimFromToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

}
