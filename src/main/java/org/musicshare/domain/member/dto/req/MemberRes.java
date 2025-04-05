package org.musicshare.domain.member.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import org.musicshare.domain.member.model.Member;

public record MemberRes(
    @Schema(description = "회원 ID", type = "String")
    Long memberId,
    @Schema(description = "닉네임", type = "String")
    String nickname
) {

    public MemberRes(Member member) {
        this(member.getId(), member.getInfo().getNickname());
    }

}
