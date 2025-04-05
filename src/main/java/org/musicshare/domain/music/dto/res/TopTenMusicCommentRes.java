package org.musicshare.domain.music.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record TopTenMusicCommentRes(
    @Schema(description = "댓글 ID", type = "Long")
    Long id,

    @Schema(description = "회원 ID", type = "Long")
    Long authorId,

    @Schema(description = "닉네임", type = "String")
    String authorNickname,

    @Schema(description = "프로필 URL", type = "String")
    String authorProfile,

    @Schema(description = "댓글 내용", type = "String")
    String content,

    @Schema(description = "평점", type = "int")
    int rating,

    @Schema(description = "댓글 좋아요 갯수", type = "int")
    int likeCount,

    @Schema(description = "댓글 등록 시간", type = "String")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime regDt
) {}