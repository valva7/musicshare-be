package org.musicshare.domain.music.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.musicshare.domain.music.dto.res.TopTenMusicCommentRes;
import org.musicshare.global.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.musicshare.domain.music.service.CommentService;

@Tag(name = "Comment", description = "댓글 관련 API")
@RestController
@RequestMapping("/comment/public")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{musicId}")
    @Operation(summary = "음악의 댓글 리스트 조회", description = "특정 음악의 댓글 리스트를 조회한다.")
    @Parameters({
        @Parameter(name = "musicId", description = "음악 ID", in = ParameterIn.PATH, required = true)
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "댓글 리스트 조회 성공", content = @Content(schema = @Schema(implementation = TopTenMusicCommentRes[].class)))
    })
    public Response<List<TopTenMusicCommentRes>> getMusicCommentList(@PathVariable Long musicId) {
        List<TopTenMusicCommentRes> topTenSelectMusicCommentList = commentService.getMusicCommentList(musicId);
        return Response.ok(topTenSelectMusicCommentList);
    }

}
