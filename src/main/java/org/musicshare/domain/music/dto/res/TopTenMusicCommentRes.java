package org.musicshare.domain.music.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record TopTenMusicCommentRes(
    Long id,
    Long authorId,
    String authorNickname,
    String authorProfile,
    String content,
    int likeCount,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime regDt
) {}