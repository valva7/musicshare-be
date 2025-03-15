package org.musicshare.domain.music.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.musicshare.domain.music.service.CommentService;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

//    @PostMapping
//    public Response<Void> createComment(
//        @Parameter(hidden = true) @AuthPrincipal UserAuth user,
//        @RequestBody CreateCommentReq req
//    ) {
//        commentService.createComment(user, req);
//    }

}
