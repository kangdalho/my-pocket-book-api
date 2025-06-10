package com.nbcamp.mypocketbookapi.config;

import com.nbcamp.mypocketbookapi.interceptor.SessionAuthenticationInterceptor;
import com.nbcamp.mypocketbookapi.resolver.LoginMemberArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final SessionAuthenticationInterceptor sessionAuthenticationInterceptor;
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionAuthenticationInterceptor)
                .addPathPatterns(
                        "/api/**"
                )
                .excludePathPatterns(
                        "/api/members/signup",
                        "/api/members/login"
                );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }
}
