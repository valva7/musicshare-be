package org.musicshare.domain.like.dto.res;

public record MusicLikedRes(
    boolean liked,
    int likeCount
) {

}
