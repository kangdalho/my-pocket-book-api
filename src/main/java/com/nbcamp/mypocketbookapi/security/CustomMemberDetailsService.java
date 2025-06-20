package com.nbcamp.mypocketbookapi.security;

import com.nbcamp.mypocketbookapi.entity.Member;
import com.nbcamp.mypocketbookapi.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Member memberData = memberJpaRepository.findByNickname(nickname);
        if (memberData != null) {
            return new CustomMemberDetails(memberData);
        }
        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + nickname);
    }
}
