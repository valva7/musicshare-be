package org.musicshare.domain.like.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

public record MusicLikedRes(
    @Schema(description = "좋아요 여부", type = "Boolean")
    boolean liked,

    @Schema(description = "좋아요 갯수", type = "int")
    int likeCount
) {

}
