package org.calendaralarm.global.config;

import java.util.List;
import org.calendaralarm.global.pricipal.AuthenticationPrincipalArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Spring MVC에서 웹 애플리케이션의 설정을 추가하고 커스터마이징할 수 있게 해주는 인터페이스
@Configuration
public class AuthConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}