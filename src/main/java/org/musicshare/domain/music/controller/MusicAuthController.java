package org.musicshare.domain.music.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.musicshare.domain.music.dto.req.MusicUploadReq;
import org.musicshare.domain.music.service.MusicService;
import org.musicshare.global.pricipal.AuthPrincipal;
import org.musicshare.global.pricipal.UserAuth;
import org.musicshare.global.response.Response;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth Music", description = "인증된 회원 음악 관련 API")
@RequestMapping("/music/auth")
@Validated
@RestController
public class MusicAuthController {

    private final MusicService musicService;

    public MusicAuthController(MusicService musicService) {
        this.musicService = musicService;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "음악 업로드",
        description = "음악을 업로드한다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "음악 업로드 성공")
        }
    )
    public Response<Void> uploadMusicFile(@Parameter(hidden = true) @AuthPrincipal UserAuth user, @ModelAttribute @Valid MusicUploadReq req) throws Exception {
        musicService.uploadMusicFile(user, req);
        return Response.ok(null);
    }

}
