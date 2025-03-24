package org.musicshare.domain.music.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.musicshare.domain.music.dto.res.MusicDetailRes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.musicshare.domain.music.dto.res.PopularMusicRes;
import org.musicshare.domain.music.service.MusicService;
import org.musicshare.global.response.Response;

@RestController
@RequestMapping("/music/public")
@AllArgsConstructor
public class MusicController {

    private final MusicService musicService;

    @GetMapping("/hot/current")
    public Response<List<PopularMusicRes>> getTop10ByCurrentMonthOrWeekOrderByLikes(
        @RequestParam(required = false) String genre
    ) {
        List<PopularMusicRes> result = musicService.getTop10ByCurrentMonthOrWeekOrderByLikes(genre);
        return Response.ok(result);
    }

    @GetMapping("/{musicId}")
    public Response<MusicDetailRes> getMusicDetail(@PathVariable Long musicId) {
        MusicDetailRes musicDetail = musicService.getMusicDetail(musicId);
        return Response.ok(musicDetail);
    }

}
