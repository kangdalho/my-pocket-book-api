package com.nbcamp.mypocketbookapi.config;

import com.nbcamp.mypocketbookapi.interceptor.AuthenticationInterceptor;
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
    private final AuthenticationInterceptor authenticationInterceptor;
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns(
                        "/api/**"
                )
                .excludePathPatterns(
                        "/api/members/signup",
                        "/api/members/login",
                        "/api/dummy-data",           // 더미 데이터 제외
                        "/api/reviews/top10",         // 좋아요 많은 리뷰 Top 10 조회 제외
                        "/api/books/**/reviews",      // ISBN 기준 리뷰 조회 제외
                        "/api/reviews"               // 전체 리뷰 조회 제외
                );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }
}
