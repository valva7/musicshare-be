package org.musicshare.domain.auth.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

public record NewAccessTokenRes(
    @Schema(description = "엑세스 토큰", type = "String")
    String accessToken
) {

}
