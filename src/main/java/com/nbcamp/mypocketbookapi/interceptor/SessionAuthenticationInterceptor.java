package com.nbcamp.mypocketbookapi.interceptor;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbcamp.mypocketbookapi.common.BaseResponse;
import com.nbcamp.mypocketbookapi.common.Const;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class SessionAuthenticationInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(Const.LOGIN_USER) == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");
            BaseResponse<Void> errorResponse = BaseResponse.fail(ErrorCode.UNAUTHORIZED,ErrorCode.UNAUTHORIZED.getMessage());
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(jsonResponse);
            return false; // false -> controller 실행 x
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
