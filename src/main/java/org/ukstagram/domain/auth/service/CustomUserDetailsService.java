package org.ukstagram.domain.auth.service;

import org.ukstagram.domain.user.model.entity.MemberEntity;
import org.ukstagram.domain.user.repository.MemberRepository;
import org.ukstagram.domain.auth.model.CustomUserDetails;
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
        MemberEntity user = repository.findCalendarUserEntityByNickname(nickname)
            .orElseThrow(() -> new UsernameNotFoundException("User not found : " + nickname));

        return new CustomUserDetails(
            user.getId(),
            user.getNickname()
        );
    }

    public UserDetails loadUserByUsername(MemberEntity entity) throws UsernameNotFoundException {
        return new CustomUserDetails(
            entity.getId(),
            entity.getNickname()
        );
    }
}