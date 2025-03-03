package org.calendaralarm.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.calendaralarm.domain.user.model.entity.CalendarUserEntity;
import org.calendaralarm.domain.user.repository.CalendarUserRepository;
import org.calendaralarm.domain.user.service.KakaoService;
import org.calendaralarm.global.model.CustomUserDetails;
import org.calendaralarm.global.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

// OncePerRequestFilter -> 매 요청마다 한 번만 실행되는 필터
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final KakaoService kakaoService;
    private final CalendarUserRepository repository;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청 헤더의 토큰 Get
        String authorizationHeader = request.getHeader("Authorization");
        if(StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring(7);
            // 토큰으로 로그인 사용자 조회
            String nickName = kakaoService.getUserFromKakao(accessToken).getKakaoAccount().getProfile().getNickName();

            // 존재할 경우 유저 정보를 SecurityContextHolder에 저장
            if(repository.existsByNickname(nickName)){
                CalendarUserEntity calendarUserEntity = repository.findCalendarUserEntityByNickname(nickName).get();

                // 커스텀한 유저 Object로 저장
                CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(calendarUserEntity.getNickname());
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

}
