package org.musicshare.domain.auth.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record MemberSignupReq(

    @Schema(description = "이메일", type = "String")
    @NotEmpty(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    String emailFull,

    @Schema(description = "비밀번호", type = "String")
    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    String password,

    @Schema(description = "비밀번호 확인", type = "String")
    @NotEmpty(message = "비밀번호 확인은 필수입니다.")
    String passwordConfirm,

    @Schema(description = "닉네임", type = "String")
    @NotEmpty(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하여야 합니다.")
    String nickname,

    @Schema(description = "전화번호", type = "String")
    @Size(max = 20, message = "전화번호는 20자 이하여야 합니다.")
    String phone

) {}