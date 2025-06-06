package com.nbcamp.mypocketbookapi.service;

import com.nbcamp.mypocketbookapi.dto.member.request.LoginRequestDto;
import com.nbcamp.mypocketbookapi.dto.member.request.WithdrawRequestDto;
import com.nbcamp.mypocketbookapi.dto.member.response.LoginResponseDto;
import com.nbcamp.mypocketbookapi.dto.member.response.MemberResponseDto;
import com.nbcamp.mypocketbookapi.dto.member.request.SignupRequestDto;
import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.exception.BusinessException;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberResponseDto signup(SignupRequestDto requestDto) {
        //이미 사용중인 이메일인지 검사
        if (memberJpaRepository.existsByEmail(requestDto.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        //이미 사용중인 닉네임인지 검사
        if (memberJpaRepository.existsByNickname(requestDto.getNickname())) {
            throw new BusinessException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Member member = new Member(requestDto.getEmail(), encodedPassword, requestDto.getNickname());
        Member savedMember = memberJpaRepository.save(member);
        return new MemberResponseDto(
                savedMember.getId(),
                savedMember.getEmail(),
                savedMember.getNickname(),
                savedMember.getCreatedAt());
    }

    public LoginResponseDto login(@Valid LoginRequestDto requestDto) {
        Member member = memberJpaRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.EMAIL_MISMATCH));
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }
        return new LoginResponseDto(member.getId(), member.getEmail(), member.getNickname());
    }

    public MemberResponseDto getMyInfo(Long memberId) {
        Member byId = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return new MemberResponseDto(byId.getId(), byId.getEmail(), byId.getNickname(), byId.getCreatedAt());
    }

    public void logout() {

    }

    public void withdraw(WithdrawRequestDto requestDto) {
        Member member = memberJpaRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.EMAIL_MISMATCH));
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }
        memberJpaRepository.delete(member);
    }
}
