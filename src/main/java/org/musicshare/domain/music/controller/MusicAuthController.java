package org.musicshare.domain.music.controller;


import lombok.AllArgsConstructor;
import org.musicshare.common.utils.FileValidator;
import org.musicshare.domain.music.service.MusicService;
import org.musicshare.global.exception.ErrorCode;
import org.musicshare.global.pricipal.AuthPrincipal;
import org.musicshare.global.pricipal.UserAuth;
import org.musicshare.global.response.Response;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/music/auth")
@AllArgsConstructor
public class MusicAuthController {

    private final MusicService musicService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<String> uploadMusicFile(@AuthPrincipal UserAuth user,
        @RequestParam("file") MultipartFile file,
        @RequestParam(name = "title", required = true) String title,
        @RequestParam(name = "genre", required = true) String genre,
        @RequestParam(name = "theme", required = true) String theme,
        @RequestParam(name = "description", required = true) String description,
        @RequestParam(name = "tags", required = true) String tags
    ) throws Exception {
        if (file.isEmpty()) {
            return Response.error(ErrorCode.INVALID_INPUT_VALUE);
        }
        if(!FileValidator.isValidFile(file)) {
            return Response.error(ErrorCode.INVALID_INPUT_VALUE);
        }

        musicService.uploadMusicFile(user, file, title, description, genre, theme, tags);
        return Response.ok("업로드 성공했습니다.");
    }

}
