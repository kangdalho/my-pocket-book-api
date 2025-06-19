package com.nbcamp.mypocketbookapi.interceptor;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbcamp.mypocketbookapi.common.BaseResponse;
import com.nbcamp.mypocketbookapi.common.Const;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;
import com.nbcamp.mypocketbookapi.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Header에서 순수한 토큰을 가져온다.
        Optional<String> tokenOpt = jwtUtil.extractToken(request);

        if (tokenOpt.isPresent()) {
            String token = tokenOpt.get();
            if (!jwtUtil.validateToken(token)) {
                return false;
            }
            request.setAttribute(Const.LOGIN_USER, jwtUtil.getClaimFromToken(token).get("memberId", Long.class));
            return HandlerInterceptor.super.preHandle(request, response, handler);
        } else {
            log.warn("[{}] {} - {}", ErrorCode.UNAUTHORIZED.getDomainType(), ErrorCode.UNAUTHORIZED.getHttpStatus().value(), ErrorCode.UNAUTHORIZED.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");
            BaseResponse<Void> errorResponse = BaseResponse.fail(ErrorCode.UNAUTHORIZED);
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(jsonResponse);
            return false;
        }
    }
}
