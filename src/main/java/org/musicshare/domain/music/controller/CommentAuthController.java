package org.musicshare.domain.music.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.musicshare.domain.music.dto.req.CreateCommentReq;
import org.musicshare.domain.music.service.CommentService;
import org.musicshare.global.pricipal.AuthPrincipal;
import org.musicshare.global.pricipal.UserAuth;
import org.musicshare.global.response.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment/auth")
@AllArgsConstructor
public class CommentAuthController {

    private final CommentService commentService;

    @PostMapping
    public Response<Void> createComment(
        @Parameter(hidden = true) @AuthPrincipal UserAuth user,
        @RequestBody CreateCommentReq req
    ) {
        commentService.createComment(user, req);
        return Response.ok(null);
    }

}
