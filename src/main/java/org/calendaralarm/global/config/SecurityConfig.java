package org.calendaralarm.global.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.calendaralarm.domain.user.repository.CalendarUserRepository;
import org.calendaralarm.global.service.CustomUserDetailsService;
import org.calendaralarm.domain.user.service.KakaoService;
import org.calendaralarm.global.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final KakaoService kakaoService;
    private final CustomUserDetailsService customUserDetailsService;  // CustomUserDetailsService 추가

    private final CalendarUserRepository repository;

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    private static final String[] AUTH_ALLOWLIST = {
        "/swagger-ui/**", "/v3/**", "/login/**", "/images/**", "/kakao/**", "/main/**"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)  throws Exception{
        http.csrf((csrf) -> csrf.disable());
        http.cors(Customizer.withDefaults());

        http.addFilterBefore(new JwtAuthFilter(kakaoService, repository, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers(AUTH_ALLOWLIST).permitAll()
            .anyRequest().authenticated());

        http.exceptionHandling((handling) -> handling.authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler));

        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin((form) -> form.disable());   // Spring Security 기본 로그인 화면 Disable

        return http.build();
    }

}
