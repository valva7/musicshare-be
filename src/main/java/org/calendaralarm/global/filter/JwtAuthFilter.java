package org.calendaralarm.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.calendaralarm.domain.auth.utils.TokenProvider;
import org.calendaralarm.domain.user.model.entity.CalendarUserEntity;
import org.calendaralarm.domain.user.repository.CalendarUserRepository;
import org.calendaralarm.domain.auth.service.KakaoService;
import org.calendaralarm.domain.auth.model.CustomUserDetails;
import org.calendaralarm.domain.auth.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

// OncePerRequestFilter -> 매 요청마다 한 번만 실행되는 필터
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final CalendarUserRepository repository;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청 헤더의 토큰 Get
        String authorizationHeader = request.getHeader("Authorization");
        if(StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring(7);
            // 토큰으로 사용자 인증
            Long userId = tokenProvider.getUserId(accessToken);
            CalendarUserEntity calendarUserEntity = repository.getCalendarUserEntityById(userId);

            // 존재할 경우 유저 정보를 SecurityContextHolder에 저장
            if(calendarUserEntity != null){
                // 커스텀한 유저 Object로 저장
                CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(calendarUserEntity);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

}
