package org.musicshare.domain.music.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

public record MusicDetailRes(
    @Schema(description = "음악 ID", type = "Long")
    Long musicId,

    @Schema(description = "아티스트 ID", type = "Long")
    Long authorId,

    @Schema(description = "아티스트 닉네임", type = "String")
    String nickname,

    @Schema(description = "아티스트 프로필 URL", type = "String")
    String profileImageUrl,

    @Schema(description = "음악 제목", type = "String")
    String title,

    @Schema(description = "테마", type = "String")
    String theme,

    @Schema(description = "분위기", type = "String")
    String mood,

    @Schema(description = "장르", type = "String")
    String genre,

    @Schema(description = "음악 재생 시간", type = "String")
    String duration,

    @Schema(description = "음악 URL", type = "String")
    String url,

    @Schema(description = "BPM", type = "int")
    int bpm,

    @Schema(description = "평점", type = "Double")
    double rating
) {

}
