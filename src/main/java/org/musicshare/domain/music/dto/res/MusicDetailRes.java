package org.musicshare.domain.music.dto.res;

public record MusicDetailRes(
    Long musicId,
    Long authorId,
    String nickname,
    String profileImageUrl,
    String title,
    String theme,
    String mood,
    String genre,
    String duration,
    int bpm,
    double rating
) {

}
