package org.musicshare.domain.music.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.musicshare.domain.music.dto.res.MusicDetailRes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.musicshare.domain.music.dto.res.PopularMusicRes;
import org.musicshare.domain.music.service.MusicService;
import org.musicshare.common.response.Response;

@Tag(name = "Music", description = "음악 관련 API")
@RequestMapping("/music/public")
@RestController
public class MusicController {

    private final MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping("/hot/current")
    @Operation(
        summary = "Top 10 음악 조회",
        description = "메인화면의 Top 10 음악을 조회한다.",
        parameters = {
            @Parameter(
                name = "genre",
                description = "장르 : #GENRE[발라드(BD), 클래식(CS), 댄스(DC), 일렉트로닉(EL), 힙합(HP), R&B(RB), 락(RK)]",
                schema = @Schema(allowableValues = {"BD", "CS", "DC", "EL", "HP", "RB", "RK"}, type = "String", nullable = true),
                in = ParameterIn.QUERY
            )
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Top 10 음악 조회 성공", content = @Content(schema = @Schema(implementation = PopularMusicRes[].class))
            )
        }
    )
    public Response<List<PopularMusicRes>> getTop10ByCurrentMonthOrWeekOrderByLikes(@RequestParam(required = false) String genre) {
        return Response.ok(musicService.getTop10ByCurrentMonthOrWeekOrderByLikes(genre));
    }

    @GetMapping("/{musicId}")
    @Operation(
        summary = "음악 세부 정보 조회",
        description = "특정 음악의 세부 정보를 조회한다.",
        parameters = {
            @Parameter(name = "musicId", description = "음악 ID", in = ParameterIn.PATH)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "음악 세부 정보 조회 성공")
        }
    )
    public Response<MusicDetailRes> getMusicDetail(@PathVariable Long musicId) {
        return Response.ok(musicService.getMusicDetail(musicId));
    }

}
