package org.musicshare.domain.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.member.service.FanService;
import org.musicshare.global.pricipal.AuthPrincipal;
import org.musicshare.global.pricipal.UserAuth;
import org.musicshare.global.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/fan")
@RequiredArgsConstructor
@Tag(name="Basics", description = "아티스트 팬 관리 API")
public class FanController {

    private final FanService fanService;

    @GetMapping
    public Response getFan(@AuthPrincipal UserAuth userAuth, @RequestParam Long artistId) {
        return Response.ok(fanService.getFan(userAuth, artistId));
    }

    @PostMapping
    public Response<Void> fanArtist(@AuthPrincipal UserAuth user, @RequestParam Long artistId) {
        fanService.fanArtist(user, artistId);
        return Response.ok(null);
    }

}
