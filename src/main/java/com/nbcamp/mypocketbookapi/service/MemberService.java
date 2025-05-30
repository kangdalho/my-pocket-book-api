package com.nbcamp.mypocketbookapi.service;

import com.nbcamp.mypocketbookapi.dto.LoginRequestDto;
import com.nbcamp.mypocketbookapi.dto.MemberResponseDto;
import com.nbcamp.mypocketbookapi.dto.SignupRequestDto;
import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;

    public MemberResponseDto signup(SignupRequestDto requestDto) {
        Member member = new Member(requestDto.getEmail(), requestDto.getPassword(), requestDto.getNickname());
        Member savedMember = memberJpaRepository.save(member);
        return new MemberResponseDto(
                savedMember.getId(),
                savedMember.getEmail(),
                savedMember.getNickname());
    }
}
