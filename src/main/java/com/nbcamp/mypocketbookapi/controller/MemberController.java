package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.common.Const;
import com.nbcamp.mypocketbookapi.common.LoginMember;
import com.nbcamp.mypocketbookapi.dto.member.request.LoginRequestDto;
import com.nbcamp.mypocketbookapi.dto.member.request.WithdrawRequestDto;
import com.nbcamp.mypocketbookapi.dto.member.response.LoginResponseDto;
import com.nbcamp.mypocketbookapi.dto.member.response.MessageResponseDto;
import com.nbcamp.mypocketbookapi.dto.member.response.MemberResponseDto;
import com.nbcamp.mypocketbookapi.dto.member.request.SignupRequestDto;
import com.nbcamp.mypocketbookapi.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(
            @Valid
            @RequestBody SignupRequestDto requestDto
    ) {
        MemberResponseDto signup = memberService.signup(requestDto);
        return new ResponseEntity<>(signup, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
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
        return new ResponseEntity<>(login, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyInfo(
            @LoginMember Long memberId
    ) {
        // 서비스에서 사용자 정보 조회
        MemberResponseDto myInfo = memberService.getMyInfo(memberId);
        return new ResponseEntity<>(myInfo, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponseDto> logout(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false); // false -> Session 새로 생성하지 않고 null 반환
        if (session != null) {
            session.invalidate(); // 세션 무효화 -> 로그아웃
        }
        return ResponseEntity.ok(new MessageResponseDto("로그아웃이 완료되었습니다."));
    }

    @DeleteMapping("/me")
    public ResponseEntity<MessageResponseDto> withdraw(
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
        return ResponseEntity.ok(new MessageResponseDto("회원탈퇴가 완료되었습니다."));
    }
}
