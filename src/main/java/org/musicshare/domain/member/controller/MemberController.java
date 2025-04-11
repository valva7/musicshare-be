package org.musicshare.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.member.dto.req.MemberRes;
import org.musicshare.domain.member.service.MemberService;
import org.musicshare.global.pricipal.AuthPrincipal;
import org.musicshare.global.pricipal.UserAuth;
import org.musicshare.common.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Member", description = "회원 관리 API")
@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    @Operation(
        summary = "회원정보 조회", description = "특정 회원 정보를 조회한다.",
        parameters = {
            @Parameter(name = "artistId", description = "회원 ID", in = ParameterIn.PATH, required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공", content = @Content(schema = @Schema(implementation = MemberRes.class)))
        }
    )
    public Response<MemberRes> getMember(@Parameter(hidden = true) @AuthPrincipal UserAuth user){
        return Response.ok(memberService.getMember(user));
    }

}
