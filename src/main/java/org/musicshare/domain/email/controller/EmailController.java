package org.musicshare.domain.email.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.musicshare.domain.email.service.EmailService;
import org.musicshare.global.response.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Email", description = "이메일 관련 API")
@RequestMapping("/email")
@Validated
@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/signUp/verify")
    @Operation(
        summary = "회원가입 이메일 인증 코드 발송",
        description = "회원가입 시 인증코드를 이메일로 발송한다.",
        parameters = {
            @Parameter(name = "email", description = "이메일", in = ParameterIn.QUERY, required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "인증코드 발송 성공")
        }
    )
    public Response<Void> sendSignUpVerifyCode(@RequestParam @NotBlank String email) {
        emailService.sendSignUpVerifyCode(email);
        return Response.ok(null);

    }

}
