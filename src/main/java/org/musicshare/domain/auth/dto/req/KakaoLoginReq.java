package org.musicshare.domain.auth.dto.req;

import jakarta.validation.constraints.NotEmpty;

public record KakaoLoginReq(
    @NotEmpty(message = "코드는 필수입니다.")
    String code,

    String fcmToken
) {

}
