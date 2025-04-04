package org.musicshare.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthService authService;


    @PostMapping("/kakao-token")
    public Response<LoginTokenRes> kakaoLogin(@RequestParam String code, @RequestParam String fcmToken) {
        LoginTokenRes loginTokenRes = authService.kakaoLogin(code, fcmToken);
        return Response.ok(loginTokenRes);
    }

//    @GetMapping("/login")
//    public Response<LoginTokenRes> login(@RequestBody String LoginReq , @RequestParam String fcmToken) {
//        //LoginTokenRes loginTokenRes = authService.login(code, fcmToken);
//        return Response.ok();
//    }

//    @GetMapping("/validate/nickname")
//    public Response<Boolean> validateNickname(@RequestParam @NotBlank String nickname) {
//        // 닉네임 중복 확인
//        return Response.ok(authService.checkNickname(nickname));
//    }

    @GetMapping("/verify/check")
    public Response<Boolean> verifyCheck(@RequestParam @NotBlank String email, @RequestParam @NotBlank String code) {
        // 인증 코드 확인
        return Response.ok(authService.checkVerifyCode(email, code));
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
