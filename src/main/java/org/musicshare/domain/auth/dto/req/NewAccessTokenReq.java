package org.musicshare.domain.auth.dto.req;

import jakarta.validation.constraints.NotEmpty;

public record NewAccessTokenReq(
    @NotEmpty(message = "리프레시 토큰은 필수입니다.")
    String refreshToken
) {

}
