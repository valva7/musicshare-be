package org.musicshare.domain.auth.service;

import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.member.repository.MemberRepository;
import org.musicshare.domain.auth.model.CustomUserDetails;
import org.musicshare.global.exception.MemberNotFoundException;
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
        Member member = repository.findMemberByNickname(nickname);
        if(member == null){
            throw new MemberNotFoundException("존재하지 않는 사용자");
        }

        return new CustomUserDetails(
            member.getId(),
            member.getInfo().getEmail(),
            member.getInfo().getNickname()
        );
    }

    public UserDetails loadUserByUsername(Member member) throws UsernameNotFoundException {
        return new CustomUserDetails(
            member.getId(),
            member.getInfo().getEmail(),
            member.getInfo().getNickname()
        );
    }
}