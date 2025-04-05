package org.musicshare.domain.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.musicshare.domain.like.dto.res.MusicLikedRes;
import org.musicshare.domain.like.service.LikeService;
import org.musicshare.global.pricipal.AuthPrincipal;
import org.musicshare.global.pricipal.UserAuth;
import org.musicshare.global.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Like", description = "좋아요 관련 API")
@RestController
@RequestMapping("/like")
@AllArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/music/{musicId}")
    @Operation(summary = "음악 좋아요", description = "음악에 좋아요를 누른다.")
    @Parameters({
        @Parameter(name = "musicId", description = "음악 ID", in = ParameterIn.PATH, required = true)
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "좋아요 등록 성공")
    })
    public Response<Void> likeMusic(@Parameter(hidden = true) @AuthPrincipal UserAuth user, @PathVariable Long musicId) {
        likeService.likeMusic(user, musicId);
        return Response.ok(null);
    }

    @GetMapping("/music/{musicId}")
    @Operation(summary = "음악 좋아요 정보 조회", description = "음악의 좋아요 정보를 조회한다.")
    @Parameters({
        @Parameter(name = "musicId", description = "음악 ID", in = ParameterIn.PATH, required = true)
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "좋아요 정보 조회 성공", content = @Content(schema = @Schema(implementation = MusicLikedRes.class)))
    })
    public Response<MusicLikedRes> getMusicLiked(@Parameter(hidden = true) @AuthPrincipal UserAuth user, @PathVariable Long musicId) {
        return Response.ok(likeService.getMusicLiked(user, musicId));
    }

}
