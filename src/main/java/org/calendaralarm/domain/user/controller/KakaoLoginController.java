package org.calendaralarm.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.calendaralarm.domain.user.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class KakaoLoginController {

    private final LoginService loginService;

    @GetMapping("/kakao/callback")
    public String callback(@RequestParam("code")String code, Model model){
        loginService.login(code);
        return "redirect:/main";
    }

}
