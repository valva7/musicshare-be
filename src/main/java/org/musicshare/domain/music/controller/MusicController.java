package org.musicshare.domain.music.controller;

import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.musicshare.domain.music.dto.res.TopTenMusicCurrentRes;
import org.musicshare.domain.music.service.MusicService;
import org.musicshare.global.exception.ErrorCode;
import org.musicshare.global.pricipal.AuthPrincipal;
import org.musicshare.global.pricipal.UserAuth;
import org.musicshare.global.response.Response;

@RestController
@RequestMapping("/music")
@AllArgsConstructor
public class MusicController {

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
        musicService.uploadMusicFile(user, file, title, genre, theme, description, tags);
        return Response.ok("업로드 성공했습니다.");
    }

    @GetMapping("/hot/current")
    public Response<List<TopTenMusicCurrentRes>> getTop10ByCurrentMonthOrWeekOrderByLikes(@Parameter(hidden = true) @AuthPrincipal UserAuth user) {

        List<TopTenMusicCurrentRes> top10ByCurrentMonthOrWeekOrderByLikes = musicService.getTop10ByCurrentMonthOrWeekOrderByLikes();

        return Response.ok(top10ByCurrentMonthOrWeekOrderByLikes);
    }

}
