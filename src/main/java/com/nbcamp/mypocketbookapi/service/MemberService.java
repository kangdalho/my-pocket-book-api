package com.nbcamp.mypocketbookapi.service;

import com.nbcamp.mypocketbookapi.dto.member.request.SignupRequestDto;
import com.nbcamp.mypocketbookapi.dto.member.request.WithdrawRequestDto;
import com.nbcamp.mypocketbookapi.dto.member.response.MemberResponseDto;
import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.exception.ErrorCode;
import com.nbcamp.mypocketbookapi.exception.member.MemberException;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberResponseDto signup(SignupRequestDto requestDto) {
        if (memberJpaRepository.existsByEmail(requestDto.getEmail())) {
            throw new MemberException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (memberJpaRepository.existsByNickname(requestDto.getNickname())) {
            throw new MemberException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Member member = new Member(requestDto.getEmail(), encodedPassword, requestDto.getNickname());
        Member savedMember = memberJpaRepository.save(member);
        return new MemberResponseDto(
                savedMember.getId(),
                savedMember.getEmail(),
                savedMember.getNickname(),
                savedMember.getCreatedAt());
    }

    public MemberResponseDto getMyInfo(Long memberId) {
        Member byId = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
        return new MemberResponseDto(byId.getId(), byId.getEmail(), byId.getNickname(), byId.getCreatedAt());
    }

    public void withdraw(WithdrawRequestDto requestDto, Long memberId) {
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new MemberException(ErrorCode.PASSWORD_MISMATCH);
        }
        memberJpaRepository.delete(member);
    }
}
