package org.musicshare.domain.auth.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record KakaoLoginReq(
    @Schema(description = "카카오 인증 코드", type = "String")
    @NotEmpty(message = "코드는 필수입니다.")
    String code,

    @Schema(description = "Fcm Push Token", type = "String")
    String fcmToken
) {

}
