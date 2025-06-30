package com.nbcamp.mypocketbookapi.config;


import com.nbcamp.mypocketbookapi.security.jwt.JwtFilter;
import com.nbcamp.mypocketbookapi.security.jwt.JwtUtil;
import com.nbcamp.mypocketbookapi.security.jwt.LoginFilter;
import com.nbcamp.mypocketbookapi.security.core.CustomMemberDetailsService;
import com.nbcamp.mypocketbookapi.security.oauth.CustomOAuth2UserService;
import com.nbcamp.mypocketbookapi.security.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomMemberDetailsService memberDetailsService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtUtil jwtUtil;

    // AuthenticationManger Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter(jwtUtil);
        loginFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return loginFilter;
    }

    // 비밀번호 암호화를 위한 PasswordEncoder Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CSRF 보호기능 비활성화 (JWT 사용 시 필요없음)
        http
                .csrf((auth) -> auth.disable());

        // REST API에서는 보통 폼 로그인을 사용하지 않고 JWT 인증을 사용하므로 비활성화
        http
                .formLogin((auth) -> auth.disable());

        // JWT 기반 인증이기 때문에 기본 인증 비활성화
        http
                .httpBasic((auth) -> auth.disable());

        // oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)))
                        .successHandler(oAuth2SuccessHandler));

        // 회원가입, 로그인, 루트경로는 모두 접근허용(permitAll())
        // 그 외 모든 요청은 인증된 사용자만 접근가능(authenticated())
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/members/signup", "/", "/api/members/login","/actuator/health").permitAll()
                        .anyRequest().authenticated());
        // JWT 필터 등록 (UsernamePasswordAuthenticationFilter 전에 실행)
        http
                .addFilterBefore(new JwtFilter(jwtUtil, memberDetailsService), UsernamePasswordAuthenticationFilter.class);


        // 로그인 필터 등록 (기본 UsernamePasswordAuthenticationFilter 위치에 LoginFilter 삽입)
        http
                .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);

        // 세션 관리 설정 - JWT 기반이므로 STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }
}
