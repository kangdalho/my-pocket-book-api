package com.nbcamp.mypocketbookapi.resolver;

import com.nbcamp.mypocketbookapi.common.Const;
import com.nbcamp.mypocketbookapi.common.LoginMember;
import com.nbcamp.mypocketbookapi.exception.BusinessException;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Object memberIdObj = request.getAttribute(Const.LOGIN_USER);

       if(memberIdObj == null) {
           throw new BusinessException(ErrorCode.INVALID_TOKEN_MEMBER_ID);
       }
       return memberIdObj;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class)
                && parameter.getParameterType().equals(Long.class);
    }
}
