package org.calendaralarm.domain.auth.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.calendaralarm.domain.auth.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class KakaoLoginController {

    private final LoginService loginService;

    // TODO: PostMapping으로 변경 > FE 리액트 변경 예정
    @GetMapping("/kakao/callback")
    public String callback(@RequestParam("code")String code, HttpSession session){
        String token = loginService.login(code);
        session.setAttribute("JWT_TOKEN", token); // 세션에 JWT 저장
        return "redirect:/main";
    }

}
