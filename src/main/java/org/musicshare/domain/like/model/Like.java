package org.musicshare.domain.like.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.musicshare.domain.member.model.Member;

@Builder
@Getter
@Setter
public class Like {

    private Member member;
    private Long targetId;
    private String targetType;

    public Like(Member member, Long targetId, String targetType) {
        this.member = member;
        this.targetId = targetId;
        this.targetType = targetType;
    }

}
