package org.musicshare.domain.like.controller;

import lombok.AllArgsConstructor;
import org.musicshare.domain.like.dto.req.LikeMusicReq;
import org.musicshare.domain.like.service.LikeService;
import org.musicshare.global.pricipal.AuthPrincipal;
import org.musicshare.global.pricipal.UserAuth;
import org.musicshare.global.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/like")
@AllArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/music")
    public Response likeMusic(@AuthPrincipal UserAuth user, @RequestBody LikeMusicReq musicId) {
        likeService.likeMusic(user, musicId);
        return Response.ok(null);
    }

    @GetMapping("/music")
    public Response getMusicLiked(@AuthPrincipal UserAuth user, @RequestParam Long musicId) {
        return Response.ok(likeService.getMusicLiked(user, musicId));
    }

}
