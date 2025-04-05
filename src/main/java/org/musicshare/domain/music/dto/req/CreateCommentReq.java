package org.musicshare.domain.music.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record CreateCommentReq(
    @Schema(description = "음악 ID", type = "String")
    @NotEmpty(message = "음악 ID는 필수입니다.")
    Long musicId,

    @Schema(description = "댓글 내용", type = "String")
    @NotEmpty(message = "댓글 내용은 필수입니다.")
    String content,

    @Schema(description = "평점", type = "int")
    @NotEmpty(message = "평점은 필수입니다.")
    int rating
) {

}
