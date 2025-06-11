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
import com.nbcamp.mypocketbookapi.service.MemberService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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

    @Operation(summary = "회원 생성", description = "새로운 회원을 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            })
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<MemberResponseDto>> signup(
            @Valid
            @RequestBody SignupRequestDto requestDto
    ) {
        MemberResponseDto signup = memberService.signup(requestDto);
        return ResponseEntity
                .ok(BaseResponse.success(ResponseCode.SUCCESS_SIGNUP,"회원가입이 완료되었습니다.", signup));
    }

    @Operation(summary = "회원 로그인", description = "아이디와 비밀번호를 사용하여 로그인하고 세션을 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공, 세션 생성"),
                    @ApiResponse(responseCode = "401", description = "로그인 실패 (잘못된 아이디 또는 비밀번호)")
            })
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponseDto>> login(
            @Valid
            @RequestBody LoginRequestDto requestDto,
            HttpServletRequest request
    ) {
        LoginResponseDto login = memberService.login(requestDto); // 로그인 검증 + 사용자 정보 반환
        /*
        Session의 디폴트 값은 true
        Session이 request에 존재하면 기존 Session 반환, 없으면 새로 생성하여 반환
         */
        HttpSession session = request.getSession(); // 세션 생성 or 반환
        //사용자 ID만 세션에 저장 (보안/용량 측면에서 효율적)
        Long id = login.getId();
        session.setAttribute(Const.LOGIN_USER, id);
        return ResponseEntity
                .ok(BaseResponse.success(ResponseCode.SUCCESS_LOGIN,"로그인을 성공하였습니다.",login));
    }

    @Operation(summary = "회원 정보 조회", description = "세션 ID를 기반으로 특정 회원의 상세 정보를 조회합니다.",
            security = {@SecurityRequirement(name = "sessionAuth")},
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
        return ResponseEntity
                .ok(BaseResponse.success(ResponseCode.SUCCESS_FIND_ME,"사용자 정보를 조회하였습니다.",myInfo));
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<MessageResponseDto>> logout(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false); // false -> Session 새로 생성하지 않고 null 반환
        if (session != null) {
            session.invalidate(); // 세션 무효화 -> 로그아웃
        }
        return ResponseEntity
                .ok(BaseResponse.success(ResponseCode.SUCCESS_LOGOUT,"로그아웃 하였습니다."));
    }

    @Operation(summary = "회원 삭제", description = "특정 회원을 삭제합니다.",
            security = {@SecurityRequirement(name = "sessionAuth")},
            responses = {
                    @ApiResponse(responseCode = "204", description = "삭제 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
            })
    @DeleteMapping("/me")
    public ResponseEntity<BaseResponse<MessageResponseDto>> withdraw(
            @Valid
            @RequestBody WithdrawRequestDto requestDto,
            @LoginMember Long memberId,
            HttpServletRequest request
    ) {
        // 서비스 로직으로 탈퇴 처리 (비밀번호 확인)
        memberService.withdraw(requestDto, memberId);

        HttpSession session = request.getSession(false); // false -> Session 새로 생성하지 않고 null 반환
        if (session != null) {
            session.invalidate(); // 세션 무효화 -> 회원탈퇴
        }
        return ResponseEntity
                .ok(BaseResponse.success(ResponseCode.SUCCESS_WITHDRAW,"회원탈퇴 되었습니다."));
    }
}
