package org.ukstagram.domain.auth.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ukstagram.domain.auth.utils.TokenProvider;
import org.ukstagram.global.exception.ErrorCode;
import org.ukstagram.global.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final TokenProvider tokenProvider;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @GetMapping("/check")
    public Response<Void> loginPage(HttpServletRequest request) {
        String token = request.getHeader("Authorization"); // 요청 헤더에서 토큰 가져오기

        if (!tokenProvider.validateToken(token)) {
            return Response.error(ErrorCode.UNAUTHORIZED);
        }

        return Response.ok(null);
    }

}
