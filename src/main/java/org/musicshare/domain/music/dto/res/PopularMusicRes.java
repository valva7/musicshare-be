package org.musicshare.domain.music.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

public record PopularMusicRes(
    @Schema(description = "아티스트 ID", type = "Long")
    Long memberId,

    @Schema(description = "아티스트 닉네임", type = "String")
    String nickname,

    @Schema(description = "아티스트 프로필 URL", type = "String")
    String profileImageUrl,

    @Schema(description = "음악 ID", type = "Long")
    Long musicId,

    @Schema(description = "음악 제목", type = "String")
    String title,

    @Schema(description = "음악 재생 시간", type = "String")
    String duration,

    @Schema(description = "장르", type = "String")
    String genre,

    @Schema(description = "분위기", type = "String")
    String mood,

    @Schema(description = "음악 URL", type = "String")
    String url,

    @Schema(description = "음악 좋아요 갯수", type = "int")
    int likeCount
) {
}
