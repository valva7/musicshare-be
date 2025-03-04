package org.calendaralarm.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.calendaralarm.domain.auth.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class KakaoLoginController {

    private final LoginService loginService;

    @PostMapping("/kakao-token")
    public String getKakaoToken(@RequestParam("code")String code){
        String token = loginService.login(code);
        return token;
    }

}
