package org.musicshare.domain.music.dto.req;

public record CreateCommentReq(Long musicId, String content, int rating) {

}
