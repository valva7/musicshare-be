package org.musicshare.domain.music.controller;

import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.musicshare.domain.music.dto.res.TopTenMusicCurrentRes;
import org.musicshare.domain.music.service.MusicService;
import org.musicshare.global.response.Response;

@RestController
@RequestMapping("/music/public")
@AllArgsConstructor
public class MusicController {

    private final MusicService musicService;

    @GetMapping("/hot/current")
    public Response<List<TopTenMusicCurrentRes>> getTop10ByCurrentMonthOrWeekOrderByLikes() {

        List<TopTenMusicCurrentRes> top10ByCurrentMonthOrWeekOrderByLikes = musicService.getTop10ByCurrentMonthOrWeekOrderByLikes();

        return Response.ok(top10ByCurrentMonthOrWeekOrderByLikes);
    }

}
