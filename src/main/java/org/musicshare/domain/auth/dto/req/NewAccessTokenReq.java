package org.musicshare.domain.auth.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record NewAccessTokenReq(
    @Schema(description = "리프레시 토큰", type = "String")
    @NotEmpty(message = "리프레시 토큰은 필수입니다.")
    String refreshToken
) {

}
