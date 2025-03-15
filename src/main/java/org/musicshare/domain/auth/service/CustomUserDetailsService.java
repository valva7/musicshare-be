package org.musicshare.domain.auth.service;

import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.member.repository.MemberRepository;
import org.musicshare.domain.auth.model.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository repository;

    public CustomUserDetailsService(MemberRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        // DB에서 사용자 조회
        Member member = repository.findMemberByNickname(nickname);
        if(member == null){
            throw new UsernameNotFoundException("User not found : " + nickname);
        }

        return new CustomUserDetails(
            member.getId(),
            member.getInfo().getNickname()
        );
    }

    public UserDetails loadUserByUsername(Member member) throws UsernameNotFoundException {
        return new CustomUserDetails(
            member.getId(),
            member.getInfo().getNickname()
        );
    }
}