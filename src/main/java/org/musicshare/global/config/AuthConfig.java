package org.musicshare.global.config;

import java.util.List;
import org.musicshare.domain.member.repository.MemberRepository;
import org.musicshare.global.pricipal.AuthenticationPrincipalArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Spring MVC에서 웹 애플리케이션의 설정을 추가하고 커스터마이징할 수 있게 해주는 인터페이스
@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;
    private final String frontUrl;

    public AuthConfig(MemberRepository memberRepository, @Value("${server-url.front}") String frontUrl) {
        this.memberRepository = memberRepository;
        this.frontUrl = frontUrl;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(memberRepository));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(frontUrl) // 허용할 출처
            .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메소드
            .allowedHeaders("*") // 허용할 헤더
            .exposedHeaders("Authorization") // 클라이언트에서 접근할 수 있는 응답 헤더
            .allowCredentials(true); // 쿠키를 허용할지 여부
    }
}