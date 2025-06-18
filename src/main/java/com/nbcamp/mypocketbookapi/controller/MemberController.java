package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.common.BaseResponse;
import com.nbcamp.mypocketbookapi.common.Const;
import com.nbcamp.mypocketbookapi.common.LoginMember;
import com.nbcamp.mypocketbookapi.common.ResponseCode;
import com.nbcamp.mypocketbookapi.dto.member.request.LoginRequestDto;
import com.nbcamp.mypocketbookapi.dto.member.request.WithdrawRequestDto;
import com.nbcamp.mypocketbookapi.dto.member.response.LoginResponseDto;
import com.nbcamp.mypocketbookapi.dto.member.response.MessageResponseDto;
import com.nbcamp.mypocketbookapi.dto.member.response.MemberResponseDto;
import com.nbcamp.mypocketbookapi.dto.member.request.SignupRequestDto;
import com.nbcamp.mypocketbookapi.security.JwtUtil;
import com.nbcamp.mypocketbookapi.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Tag(name = "회원 관리", description = "회원 관련 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "회원 생성", description = "새로운 회원을 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            })
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<MemberResponseDto>> signup(
            @Valid @RequestBody SignupRequestDto requestDto
    ) {
        MemberResponseDto signup = memberService.signup(requestDto);
        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_SIGNUP, signup));
    }

    @Operation(summary = "회원 로그인", description = "아이디와 비밀번호를 사용하여 로그인하고 JWT 토큰을 발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공, 세션 생성"),
                    @ApiResponse(responseCode = "401", description = "로그인 실패 (잘못된 아이디 또는 비밀번호)")
            })
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto requestDto,
            HttpServletResponse httpServletResponse
    ) {
        LoginResponseDto response = memberService.login(requestDto); // 로그인 검증 + 사용자 정보 반환
        String createToken = jwtUtil.createToken(response.getNickname(), response.getId());

        httpServletResponse.setHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);

        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_LOGIN, response));
    }

    @Operation(summary = "회원 정보 조회", description = "JWT 토큰을 기반으로 인증된 회원 정보를 조회합니다.",
            security = {@SecurityRequirement(name = "jwtAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
            })
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<MemberResponseDto>> getMyInfo(
            @Parameter(hidden = true)
            @LoginMember Long memberId
    ) {
        // 서비스에서 사용자 정보 조회
        MemberResponseDto myInfo = memberService.getMyInfo(memberId);
        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_FIND_ME, myInfo));
    }

    @Operation(summary = "회원 로그아웃", description = "프론트에서 토큰 삭제로 처리하세요.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
            })
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<MessageResponseDto>> logout() {
        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_LOGOUT));
    }

    @Operation(summary = "회원 삭제", description = "JWT 토큰 기반 인증을 통해 현재 로그인된 회원을 삭제합니다.",
            security = {@SecurityRequirement(name = "jwtAuth")},
            responses = {
                    @ApiResponse(responseCode = "204", description = "삭제 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
            })
    @DeleteMapping("/me")
    public ResponseEntity<BaseResponse<MessageResponseDto>> withdraw(
            @Valid @RequestBody WithdrawRequestDto requestDto,
            @LoginMember Long memberId
    ) {
        // 서비스 로직으로 탈퇴 처리 (비밀번호 확인)
        memberService.withdraw(requestDto, memberId);
        return ResponseEntity.ok(BaseResponse.success(ResponseCode.SUCCESS_WITHDRAW));
    }
}
