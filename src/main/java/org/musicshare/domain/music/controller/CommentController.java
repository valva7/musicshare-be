package org.musicshare.domain.music.controller;


import java.util.List;
import lombok.AllArgsConstructor;
import org.musicshare.domain.music.dto.res.TopTenMusicCommentRes;
import org.musicshare.global.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.musicshare.domain.music.service.CommentService;

@RestController
@RequestMapping("/comment/public")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public Response<List<TopTenMusicCommentRes>> getTopTenSelectMusicCommentList(
        @RequestParam Long musicId
    ) {
        List<TopTenMusicCommentRes> topTenSelectMusicCommentList = commentService.getTopTenSelectMusicCommentList(musicId);
        return Response.ok(topTenSelectMusicCommentList);
    }

}
