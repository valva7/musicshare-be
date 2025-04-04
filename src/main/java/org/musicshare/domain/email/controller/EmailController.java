package org.musicshare.domain.email.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.email.service.EmailService;
import org.musicshare.global.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Tag(name="Basics", description = "Email 전송 API")
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/signUp/verify")
    public Response sendSignUpVerifyCode(@RequestParam String email) {
        emailService.sendSignUpVerifyCode(email);
        return Response.ok(null);

    }

}
