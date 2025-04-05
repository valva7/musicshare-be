package org.musicshare.domain.auth.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record LoginReq(

    @Schema(description = "이메일", type = "String")
    @NotEmpty(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    String email,

    @Schema(description = "비밀번호", type = "String")
    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    String password,

    @Schema(description = "Fcm Push Token", type = "String")
    String fcmToken

) {

}
