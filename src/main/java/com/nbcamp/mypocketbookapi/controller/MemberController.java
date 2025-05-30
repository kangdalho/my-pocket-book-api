package com.nbcamp.mypocketbookapi.controller;

import com.nbcamp.mypocketbookapi.dto.MemberResponseDto;
import com.nbcamp.mypocketbookapi.dto.SignupRequestDto;
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
}
