package org.musicshare.domain.member.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class Member {

    private Long id;
    private MemberInfo info;

    public Member(Long id, MemberInfo info) {
        this.id = id;
        this.info = info;
    }

}
