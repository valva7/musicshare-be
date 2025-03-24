package org.musicshare.domain.music.dto.res;

public record PopularMusicRes(
    Long memberId,
    String nickname,
    String profileImageUrl,
    Long musicId,
    String title,
    String duration,
    String genre,
    String mood,
    String url,
    int likeCount) {

}
