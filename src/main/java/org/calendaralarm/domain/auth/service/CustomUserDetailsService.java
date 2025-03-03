package org.calendaralarm.domain.auth.service;

import org.calendaralarm.domain.user.model.entity.CalendarUserEntity;
import org.calendaralarm.domain.user.repository.CalendarUserRepository;
import org.calendaralarm.domain.auth.model.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CalendarUserRepository repository;

    public CustomUserDetailsService(CalendarUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        // DB에서 사용자 조회
        CalendarUserEntity user = repository.findCalendarUserEntityByNickname(nickname)
            .orElseThrow(() -> new UsernameNotFoundException("User not found : " + nickname));

        return new CustomUserDetails(
            user.getId(),
            user.getNickname()
        );
    }

    public UserDetails loadUserByUsername(CalendarUserEntity entity) throws UsernameNotFoundException {
        return new CustomUserDetails(
            entity.getId(),
            entity.getNickname()
        );
    }
}