package org.musicshare.domain.member.dto.req;

import org.musicshare.domain.member.model.Member;

public record MemberResDto(Long userId, String nickname) {

    public MemberResDto(Member member) {
        this(member.getId(), member.getInfo().getNickname());
    }

}
