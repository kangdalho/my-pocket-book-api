package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.dto.member.request.LoginRequestDto;
import com.nbcamp.mypocketbookapi.dto.member.response.LoginResponseDto;
import com.nbcamp.mypocketbookapi.dto.member.response.MemberResponseDto;
import com.nbcamp.mypocketbookapi.dto.member.request.SignupRequestDto;
import com.nbcamp.mypocketbookapi.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody LoginRequestDto requestDto
    ) {
        LoginResponseDto login = memberService.login(requestDto);
        return new ResponseEntity<>(login, HttpStatus.OK);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> getMyInfo(
            @PathVariable Long memberId
    ) {
        MemberResponseDto myInfo = memberService.getMyInfo(memberId);
        return new ResponseEntity<>(myInfo,HttpStatus.OK);
    }
}
