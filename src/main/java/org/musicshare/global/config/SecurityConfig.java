package org.musicshare.global.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.musicshare.domain.auth.service.TokenProvider;
import org.musicshare.domain.member.repository.MemberRepository;
import org.musicshare.domain.auth.service.CustomUserDetailsService;
import org.musicshare.global.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] AUTH_ALLOWLIST = {
        "/swagger-ui/**",
        "/v3/**",
        "/login/**",
        "/images/**",
        "/main/**",
        "/auth/**",
        "/music/public/**",
        "/comment/public/**",
        "/email/signUp/verify",
        "/common/**",
        "/actuator/prometheus", // 인증 추가 예정
        "/actuator/**", // 인증 추가 예정
    };

    private final MemberRepository repository;

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final TokenProvider tokenProvider;

    public SecurityConfig(
        MemberRepository repository,
        CustomUserDetailsService customUserDetailsService,
        CustomAuthenticationEntryPoint authenticationEntryPoint,
        CustomAccessDeniedHandler accessDeniedHandler,
        TokenProvider tokenProvider
    ) {
        this.repository = repository;
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)  throws Exception{
        // CSRF 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // CORS 설정
        http.cors(Customizer.withDefaults());

        // AUTH_ALLOWLIST에 포함된 경로는 인증 없이 허용
        http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers(AUTH_ALLOWLIST).permitAll() // allow list
            .anyRequest().authenticated()); // 나머지 경로는 인증 필요

        // JWT 인증 필터 추가 (requestMatchers로 설정한 경로 외에서 동작하도록)
        http.addFilterBefore(new JwtAuthFilter(customUserDetailsService, tokenProvider, repository), UsernamePasswordAuthenticationFilter.class);

        // 예외 처리
        http.exceptionHandling(handling -> handling
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler));

        // 세션 관리 (stateless)
        http.sessionManagement(sessionManagement -> sessionManagement
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Spring Security 기본 로그인 화면 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(AUTH_ALLOWLIST);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
