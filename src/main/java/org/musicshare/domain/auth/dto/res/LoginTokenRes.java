package org.musicshare.domain.auth.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginTokenRes(
    @Schema(description = "AccessToken", type = "String")
    String accessToken,

    @Schema(description = "RefreshToken", type = "String")
    String refreshToken
) {
}
