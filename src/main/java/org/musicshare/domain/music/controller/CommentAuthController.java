package org.musicshare.domain.music.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.musicshare.domain.music.dto.req.CreateCommentReq;
import org.musicshare.domain.music.service.CommentService;
import org.musicshare.global.pricipal.AuthPrincipal;
import org.musicshare.global.pricipal.UserAuth;
import org.musicshare.global.response.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth Comment", description = "인증된 회원 댓글 관련 API")
@RequestMapping("/comment/auth")
@Validated
@RestController
public class CommentAuthController {

    private final CommentService commentService;

    public CommentAuthController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @Operation(
        summary = "댓글 등록",
        description = "특정 음악에 댓글을 등록한다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "댓글 등록 성공")
        }
    )
    public Response<Void> createComment(@Parameter(hidden = true) @AuthPrincipal UserAuth user, @RequestBody @Valid CreateCommentReq req) {
        commentService.createComment(user, req);
        return Response.ok(null);
    }

}
