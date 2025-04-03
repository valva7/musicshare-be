package org.musicshare.domain.like.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.musicshare.domain.member.model.Member;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Like {

    private Member member;
    private Long targetId;
    private String targetType;

}
