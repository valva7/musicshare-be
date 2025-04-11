package org.musicshare.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.member.service.FanService;
import org.musicshare.global.pricipal.AuthPrincipal;
import org.musicshare.global.pricipal.UserAuth;
import org.musicshare.global.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Fan", description = "팬 관련 API")
@Slf4j
@RestController
@RequestMapping("/fan")
public class FanController {

    private final FanService fanService;

    public FanController(FanService fanService) {
        this.fanService = fanService;
    }

    @GetMapping("/{artistId}")
    @Operation(
        summary = "팬 여부 확인",
        description = "특정 아티스트의 팬인지 확인한다.",
        parameters = {
            @Parameter(name = "artistId", description = "회원 ID", in = ParameterIn.PATH, required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "팬 여부 조회 성공", content = @Content(schema = @Schema(implementation = Boolean.class)))
        }
    )
    public Response<Boolean> getFan(@Parameter(hidden = true) @AuthPrincipal UserAuth userAuth, @PathVariable Long artistId) {
        return Response.ok(fanService.getFan(userAuth, artistId));
    }

    @PostMapping("/{artistId}")
    @Operation(
        summary = "팬 등록",
        description = "특정 아티스트의 팬으로 등록한다.",
        parameters = {
            @Parameter(name = "artistId", description = "회원 ID", in = ParameterIn.PATH, required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "팬 등록 성공")
        }
    )
    public Response<Void> fanArtist(@Parameter(hidden = true) @AuthPrincipal UserAuth user, @PathVariable Long artistId) {
        fanService.fanArtist(user, artistId);
        return Response.ok(null);
    }

}
