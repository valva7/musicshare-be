package org.musicshare.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.auth.dto.req.KakaoLoginReq;
import org.musicshare.domain.auth.dto.req.LoginReq;
import org.musicshare.domain.auth.dto.req.MemberSignupReq;
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

@Tag(name = "Auth", description = "인증 관련 API")
@Slf4j
@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthService authService;


    @PostMapping("/kakao-token")
    @Operation(summary = "카카오 로그인", description = "프론트로부터 전달받은 코드로 카카로 로그인한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "카카오 로그인 성공", content = @Content(schema = @Schema(implementation = LoginTokenRes.class)))
    })
    public Response<LoginTokenRes> kakaoLogin(@RequestBody @Valid KakaoLoginReq req) {
        LoginTokenRes loginTokenRes = authService.kakaoLogin(req);
        return Response.ok(loginTokenRes);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = LoginTokenRes.class)))
    })
    public Response<LoginTokenRes> login(@RequestBody @Valid LoginReq req) {
        LoginTokenRes loginTokenRes = authService.login(req);
        return Response.ok(loginTokenRes);
    }

    @PostMapping("/signUp")
    @Operation(summary = "회원가입", description = "회원가입을 한다")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원가입 성공")
    })
    public Response<Void> signUp(@RequestBody @Valid MemberSignupReq req) {
        authService.signUp(req);
        return Response.ok(null);
    }

    @GetMapping("/validate/nickname")
    @Operation(summary = "닉네임 중복체크", description = "회원가입 시 닉네임을 중복 체크한다.")
    @Parameters({
        @Parameter(name = "nickname", description = "닉네임", in = ParameterIn.QUERY, required = true)
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "닉네임 중복체크 성공", content = @Content(schema = @Schema(implementation = Boolean.class)))
    })
    public Response<Boolean> validateNickname(@RequestParam @NotBlank String nickname) {
        return Response.ok(authService.validateNickname(nickname));
    }

    @GetMapping("/verify/check")
    @Operation(summary = "회원가입 이메일 인증", description = "회원가입 시 이메일로 전달한 인증코드를 확인한다.")
    @Parameters({
        @Parameter(name = "email", description = "이메일", in = ParameterIn.QUERY, required = true),
        @Parameter(name = "code", description = "인증코드", in = ParameterIn.QUERY, required = true)
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "인증코드 인증 여부 확인 성공", content = @Content(schema = @Schema(implementation = Boolean.class)))
    })
    public Response<Boolean> verifyCheck(@RequestParam @NotBlank String email, @RequestParam @NotBlank String code) {
        return Response.ok(authService.checkVerifyCode(email, code));
    }

    @GetMapping("/check")
    @Operation(summary = "AccessToken 확인", description = "AccessToken이 유효한지 확인한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유효 여부 확인 성공")
    })
    public Response<Void> loginPage(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (!tokenProvider.validateToken(token)) {
            return Response.error(ErrorCode.UNAUTHORIZED);
        }

        return Response.ok(null);
    }

    @PostMapping("/refresh")
    @Operation(summary = "AccessToken 재발급", description = "RefreshToken으로 AccessToken을 재발급한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "AccessToken 발급 성공", content = @Content(schema = @Schema(implementation = NewAccessTokenRes.class)))
    })
    public Response<NewAccessTokenRes> refreshAccessToken(@RequestBody @Valid NewAccessTokenReq request) {
        String refreshToken = request.refreshToken();

        if (tokenProvider.validateToken(refreshToken)) {
            String newAccessToken = tokenProvider.createNewAccessToken(refreshToken);
            return Response.ok(new NewAccessTokenRes(newAccessToken));
        } else {
            return Response.error(ErrorCode.UNAUTHORIZED);
        }
    }

}
