package org.musicshare.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.musicshare.domain.auth.dto.req.NewAccessTokenReq;
import org.musicshare.domain.auth.dto.res.LoginTokenRes;
import org.musicshare.domain.auth.dto.res.NewAccessTokenRes;
import org.musicshare.domain.auth.service.AuthService;
import org.musicshare.domain.auth.utils.TokenProvider;
import org.musicshare.global.exception.ErrorCode;
import org.musicshare.global.response.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${kakao.client_id}")
    private String clientId;
    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    private final TokenProvider tokenProvider;
    private final AuthService authService;


    @PostMapping("/kakao-token")
    public Response<LoginTokenRes> getKakaoToken(@RequestParam("code")String code){
        LoginTokenRes loginTokenRes = authService.login(code);
        return Response.ok(loginTokenRes);
    }

    @GetMapping("/check")
    public Response<Void> loginPage(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (!tokenProvider.validateToken(token)) {
            return Response.error(ErrorCode.UNAUTHORIZED);
        }

        return Response.ok(null);
    }

    @PostMapping("/refresh")
    public Response<?> refreshAccessToken(@RequestBody NewAccessTokenReq request) {
        String refreshToken = request.refreshToken();

        if (tokenProvider.validateToken(refreshToken)) {
            String newAccessToken = tokenProvider.createNewAccessToken(refreshToken);
            return Response.ok(new NewAccessTokenRes(newAccessToken));
        } else {
            return Response.error(ErrorCode.UNAUTHORIZED);
        }
    }

}
